# Sub-Agents Episode — UI Planner & UI Implementer
**Aviz Academy | Claude Code for Developers**

---

## Overview

This document covers the two custom sub-agents we build in this episode:

- **`ui-planner`** — Analyzes the existing UI and produces a structured change plan
- **`ui-implementer`** — Reads the plan and makes the actual code changes

Both agents live in `.claude/agents/` inside the project folder and are automatically available to Claude Code once the files are created.

---

## What Are Sub-Agents?

A sub-agent is an **isolated Claude instance** with its own context window. You give it a task, it does the work independently, and returns only the result — not all the noise in between.

Think of it like being a tech lead. Instead of doing every side task yourself and cluttering your own headspace, you delegate to a specialist. They go off, do the job, and come back with just the answer.

```
Main Session (You)
│
├──► Sub-Agent A  ← own context, own tools, own focus
├──► Sub-Agent B  ← own context, own tools, own focus
│
└──► Only the results come back to you
```

Sub-agent files live as markdown in `.claude/agents/` and are automatically available to Claude Code once created.

---

## Why Do We Need Sub-Agents?

| Problem Without Sub-Agents | How Sub-Agents Solve It |
|---|---|
| Context window fills up with file reading noise | Each sub-agent has its own fresh context window |
| Side tasks block your main work | Sub-agents can run independently (and in parallel) |
| Same specialist task repeated across sessions | Define once in `.claude/agents/`, reuse forever |
| Too many tools in one session = higher risk | Each sub-agent gets only the tools it needs |
| One model for everything = expensive | Route simple tasks to Haiku, complex ones to Sonnet |
| Hard to share workflows across a team | Project-level agents in `.claude/agents/` are shared via Git |

The core principle is the same one you already teach in DevSecOps: **least privilege**. A planning agent should never have write access. A read-only reviewer should never be able to run bash commands. Sub-agents enforce this at the agent level.

---

## Built-in Sub-Agents

Claude Code ships with built-in sub-agents that it uses automatically — you don't need to configure them. They kick in behind the scenes when Claude decides they're the right tool for the job.

| Built-in Agent | Purpose | When Claude Uses It |
|---|---|---|
| **Explore** | Fast, read-only codebase search and analysis | When Claude needs to understand your code before making changes |
| **Plan** | Research and context gathering for plan mode | When you run in plan mode and Claude needs to explore before proposing a plan |
| **General-purpose** | Flexible sub-agent for delegated tasks | When Claude spawns a worker for any isolated subtask |

You've already seen these in action — every time Claude says "let me explore the codebase first" before making a change, it's using the Explore sub-agent under the hood.

Custom sub-agents (like the ones we build in this episode) work the same way, but with your own system prompt, tool restrictions, and model choice.

---

## How Sub-Agents Are Created

You do NOT write the agent files manually. Instead, you use the `/agents` command in Claude Code and provide a prompt describing what the agent should do. Claude Code generates the full agent file for you.

```
/agents
```

Select **"Create new agent"**, then paste the prompt for each agent below.

---

## Agent 1 — UI Planner

### What It Does

- Reads the existing `index.html` and `style.css`
- Understands the current UI structure
- Produces a clear, structured plan of what to change and where
- **Never writes or edits any files** — read-only by design

### Prompt to Give Claude When Creating This Agent

```
Create a sub-agent called "ui-planner".

This agent analyzes the existing UI of a Flask web application and produces a structured improvement plan. It must never write or edit any files — it is a read-only planning agent.

The project uses:
- templates/index.html — the only HTML template (Jinja2)
- static/style.css — plain hand-written CSS, no frameworks

The agent should:
1. Read index.html and style.css fully before forming any opinion
2. Identify specific UI improvements — layout, spacing, colors, typography, usability
3. Always include this as one of the planned changes:
   - Add an Aviz Academy branded footer containing:
     * Text: "© Aviz Academy" with the current year
     * Links: YouTube, LinkedIn, GitHub (placeholder hrefs are fine)
     * Styling: simple and minimal — consistent with the existing plain CSS aesthetic
     * Position: static at the bottom of the page
4. Output a numbered, structured plan with:
   - What to change
   - Which file and which section
   - Why the change improves the UI
5. Keep the plan concrete and implementable — no vague suggestions
6. End with a summary: total changes proposed, estimated effort (low/medium/high)

Tool access: Read, Glob, Grep only.
Model: claude-sonnet-4-5
```

---

## Agent 2 — UI Implementer

### What It Does

- Receives the plan produced by `ui-planner` (passed in by Claude Code)
- Makes the actual edits to `index.html` and `style.css`
- Follows the plan strictly — no improvising or adding unplanned changes
- Reports what it changed after completing the work

### Prompt to Give Claude When Creating This Agent

```
Create a sub-agent called "ui-implementer".

This agent receives a UI improvement plan and implements it by editing the project's HTML and CSS files. It must follow the plan strictly and not make any unplanned changes.

The project uses:
- templates/index.html — Jinja2 template, edit carefully (preserve all {{ }} template syntax)
- static/style.css — plain CSS, no frameworks

The agent should:
1. Read the plan provided in the task prompt carefully before making any changes
2. Read the current state of index.html and style.css before editing
3. Implement each planned change one at a time
4. Preserve all existing Jinja2 template syntax ({{ }}, {% %}) — never break template logic
5. Never add JavaScript frameworks, CSS frameworks, or external libraries
6. After completing all changes, output a summary:
   - List of files changed
   - What was changed in each file
   - Anything skipped and why

Tool access: Read, Write, Edit, Glob.
Model: claude-sonnet-4-5
```

---

## How the Agents Work Together (The Flow)

```
You (Main Session)
│
│  "Improve the Task Manager UI"
▼
Claude Code (Orchestrator)
│
├──► ui-planner (own context window)
│         Reads index.html + style.css
│         Produces structured plan
│         Returns: numbered list of changes
│
├──► ui-implementer (own context window)
│         Receives the plan as input
│         Edits index.html + style.css
│         Returns: summary of what changed
│
└──► Your main session gets the final summary
     (none of the file reading/editing noise is in your context)
```

---

## How to Invoke the Agents

### Option 1 — Let Claude Decide (Auto-Invoke)

Just describe the task. Claude reads each agent's description and delegates automatically.

```
Improve the UI of this Task Manager app
```

### Option 2 — Explicit Invoke by Name

```
Use the ui-planner agent to analyze the current UI and suggest improvements
```

```
Use the ui-implementer agent to apply the plan from ui-planner
```

### Option 3 — Both in One Instruction (Sequential)

```
Use ui-planner to analyze the UI, then pass the plan to ui-implementer to apply the changes
```

---

## Where the Agent Files Live

```
task-management/
└── .claude/
    ├── agents/
    │   ├── ui-planner.md        ← created by /agents command
    │   └── ui-implementer.md    ← created by /agents command
    ├── commands/
    │   └── avinash-security.md
    └── settings.json
```

---

## On-Camera Demo Flow (Suggested)

1. Open terminal in the project folder
2. Run `claude` to start Claude Code
3. Type `/agents` → show the current list (empty or default only)
4. Select "Create new agent" → paste the `ui-planner` prompt → confirm
5. Repeat for `ui-implementer`
6. Show the generated `.claude/agents/` files briefly
7. Type: **"Use ui-planner to analyze the current UI and suggest improvements"**
8. Show the plan output in the terminal
9. Type: **"Now use ui-implementer to apply those changes"**
10. Show the summary output
11. Refresh the browser → reveal the updated UI

---

## Key Teaching Points for the Episode

| Point | What to Say On Camera |
|---|---|
| Read-only by design | "The Planner can't write files — this is intentional. Planning and doing are separated." |
| Context isolation | "All that file reading happens in its own context window — your main session stays clean." |
| Passing output between agents | "The Planner's output becomes the Implementer's input — that's the handoff." |
| Tool restriction | "The Implementer has Write access, the Planner doesn't. Least privilege, just like in DevSecOps." |
| Reusability | "Once these agents are in .claude/agents/, every developer on the team gets them automatically." |

---

## Next Episode Teaser

> "Now that we know how to plan and implement with sub-agents — what if we could verify the result automatically in a real browser? That's exactly what we'll do with Playwright in the next episode."
