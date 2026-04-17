# Claude Code — Slash Commands Complete Reference
> **Aviz Academy | Claude Code Series**  
> Episode: Slash Commands Deep Dive  
> Target Audience: Developers of all backgrounds

---

## 🧭 What Are Slash Commands?

Slash commands are **typed shortcuts** that start with `/` and control your Claude Code session directly. Think of them as **control panel buttons** — they don't ask Claude to write code; they manage *how the session itself behaves*.

```
Regular prompt   →  "Add a login feature"          (asks Claude to do work)
Slash command    →  /clear                          (controls the session)
effore mode set  →  /effort
```

> 💡 **Rule of Thumb:** If you're managing memory, context, config, or session state — use a slash command.

---

## 📋 Built-in Slash Commands (Complete List)

### 🔵 SESSION MANAGEMENT

| Command | Purpose | When to Use |
|--------|---------|------------|
| `/clear` | Wipes **all** conversation history. Fresh start. | Switching to a completely different task |
| `/compact` | Compresses history into a **summary**. Keeps context alive. | Long sessions where Claude starts forgetting earlier decisions |
| `/exit` or `/quit` | Closes the Claude Code session cleanly | Done for the day / switching projects |

---

#### `/clear` — Reset Everything

```
> /clear
```

- **What it does:** Deletes the entire conversation history from Claude's memory.
- **Important:** File edits made during the session **are not undone** — only the chat context is cleared.
- **Think of it as:** Closing a tab and opening a new one.

**Best practice:** Use `/clear` every time you start a new task. Don't carry over unrelated context.

---

#### `/compact` — Compress, Don't Delete

```
> /compact
```

- **What it does:** Replaces the full chat history with a compressed AI-generated summary.
- **Why it matters:** Claude Code has a **context window limit** (max tokens it can hold). Long sessions hit this ceiling, causing Claude to "forget" earlier decisions.
- **Think of it as:** Summarizing meeting notes instead of re-reading 3 hours of transcripts.

**`/clear` vs `/compact` — Quick Comparison:**

| | `/clear` | `/compact` |
|--|--------|----------|
| History after | Empty | Compressed summary |
| Context preserved | ❌ None | ✅ Key points |
| Use when | Switching tasks | Same task, long session |
| Tokens after | 0 | Low |

---

### 🟢 SETUP & CONFIGURATION

| Command | Purpose | When to Use |
|--------|---------|------------|
| `/init` | Scans the project and creates `CLAUDE.md` | First time in any project |
| `/memory` | Opens `CLAUDE.md` for editing | Adding/updating project rules mid-session |
| `/config` | Opens settings menu (model, permissions, tools) | Tweaking behaviour without restarting |
| `/model` | Switch the AI model mid-session | Complex tasks → Opus, Fast tasks → Haiku |
| `/permissions` | Manage what tools Claude is allowed to use | Controlling access for safety |

---

#### `/init` — Project Onboarding (You already covered this!)

```
> /init
```

- Scans your codebase, reads `README.md`, and generates `CLAUDE.md`.
- Acts as Claude's **persistent memory** for the project.

---

#### `/memory` — Edit Rules On-The-Fly

```
> /memory
```

- Opens `CLAUDE.md` directly for editing during a live session.
- Great for adding rules mid-session (e.g., "Always use pytest for tests").
- Claude picks up the updated rules **immediately** in the same session.

---

#### `/model` — Switch Models Without Restarting

```
> /model claude-opus-4-5
> /model claude-haiku-4-5
> /model claude-sonnet-4-5
```

| Model | Best For |
|-------|---------|
| `claude-opus-4-5` | Complex reasoning, architecture decisions |
| `claude-sonnet-4-5` | Balanced — good default |
| `claude-haiku-4-5` | Fast, repetitive, mechanical tasks |

> 💡 **Cost tip:** Use Haiku for bulk/simple tasks to save API tokens.

---

### 🟡 INFORMATION & DIAGNOSTICS

| Command | Purpose | When to Use |
|--------|---------|------------|
| `/help` | Lists **all** available commands (including custom ones) | When you're unsure what's available |
| `/cost` | Shows token usage and cost for the current session | Monitoring spend |
| `/status` | Displays session status and environment info | Diagnosing issues |
| `/context` | Shows how much of the context window is used | Before deciding to `/compact` |
| `/doctor` | Runs a health check on your Claude Code setup | After installation or config changes |

---

#### `/help` — Your Always-Updated Reference

```
> /help
```

- Displays all built-in commands **plus** any custom commands you've created.
- Since Claude Code updates frequently, `/help` is always the **authoritative** source.

---

#### `/cost` — Track Your Token Spend

```
> /cost
```

- Shows how many tokens you've used and the estimated API cost for the current session.
- Helps you decide when to `/compact` (save tokens) or `/clear` (start fresh).

---

#### `/context` — Check Context Window Usage

```
> /context
```

- Shows what percentage of the context window is filled.
- Use this before running `/compact` to see if compression is needed.

---

### 🔴 CODE & WORKFLOW COMMANDS

| Command | Purpose | When to Use |
|--------|---------|------------|
| `/review` | Triggers a structured code review | Before committing important changes |
| `/diff` | Shows a diff of recent file changes | Inspect what Claude has changed |
| `/undo` | Reverts the **last** file change made by Claude | Quickly rollback a bad edit |
| `/plan` | Toggles **Plan Mode** — Claude proposes before acting | For risky operations you want to approve first |
| `/debug` | Bundled skill — traces and debugs an issue | When you need structured debugging help |

---

#### `/undo` — Instant Rollback

```
> /undo
```

- Reverts the last file edit made by Claude in the session.
- Does **not** undo multiple steps — just the most recent change.
- For bigger rollbacks, use `git`.

---

#### `/plan` — Review Before Claude Acts

```
> /plan
```

- Switches Claude into **Plan Mode**: Claude describes what it *will* do and waits for your approval.
- Toggle it on/off with `/plan` or `Shift+Tab`.
- Great for production code changes where you want oversight.

---

### 🟣 INTEGRATIONS & EXTENSIONS

| Command | Purpose | When to Use |
|--------|---------|------------|
| `/mcp` | Manage connected MCP (Model Context Protocol) servers | Adding external tool integrations |
| `/install-github-app` | Connects Claude to GitHub for automatic PR reviews | CI/CD and PR automation workflows |
| `/hooks` | Configure event-based automation hooks | Auto-running linters, formatters on file changes |
| `/agents` | Manage configured sub-agents | Delegating specialized tasks to focused agents |

---

#### `/mcp` — Connect External Tools

```
> /mcp
```

- MCP (Model Context Protocol) servers expose tools as slash commands.
- Example: Connect a GitHub MCP server → commands like `/mcp__github__list_prs` become available.

---

### 🔧 CUSTOM SLASH COMMANDS

You can create your own slash commands for repeated workflows.

**How it works:**
1. Create a folder: `.claude/commands/` (project-level) or `~/.claude/commands/` (global)
2. Add a `.md` file — the filename becomes the command name
3. Write the prompt inside the file in plain English

**Example — Create `/review-security`:**

```bash
mkdir -p .claude/commands
cat > .claude/commands/review-security.md << 'EOF'
---
description: Run a security vulnerability scan on the codebase
allowed-tools: Read, Grep, Glob
---
Analyze the codebase for security vulnerabilities including:
- SQL injection risks
- XSS vulnerabilities
- Exposed credentials
- Insecure configurations

Report findings by severity: Critical → High → Medium → Low.
EOF
```

**Use it:**
```
> /review-security
```

**Use arguments:**
```markdown
# .claude/commands/fix-issue.md
Fix issue #$1 with priority $2. Check the description and implement the fix.
```
```
> /fix-issue 42 high
```

---

## ⌨️ Keyboard Shortcuts (Bonus)

| Shortcut | What It Does |
|----------|-------------|
| `Shift+Tab` | Toggle Plan Mode on/off |
| `Ctrl+C` | Cancel the current Claude action |
| `Ctrl+R` | Search session history |
| `Ctrl+L` | Clear terminal display (keeps conversation context) |
| `Ctrl+D` | Exit session cleanly |
| `↑ / ↓` arrows | Navigate previous inputs |
| `Tab` | Toggle thinking / autocomplete |
| `# text` | Quick memory — adds a note to session context instantly |
| `@path` | File autocomplete — reference a file in your prompt |
| `! command` | Run a shell command directly without asking Claude |

---

## 🗂️ Quick-Reference Cheat Sheet

```
SESSION CONTROL
  /clear        → Wipe history. Fresh start.
  /compact      → Compress history. Keep context.
  /exit /quit   → Close Claude Code session.

SETUP & MEMORY
  /init         → Generate CLAUDE.md for the project.
  /memory       → Edit CLAUDE.md mid-session.
  /config       → Open settings menu.
  /model        → Switch AI model (Opus/Sonnet/Haiku).

INFO & DIAGNOSTICS
  /help         → List all commands.
  /cost         → See token usage & spend.
  /context      → Check context window fill %.
  /status       → Session health info.
  /doctor       → Full setup health check.

CODE & REVIEW
  /review       → Code review.
  /diff         → See recent file changes.
  /undo         → Revert last file edit.
  /plan         → Plan Mode (approve before acting).
  /debug        → Structured debugging session.

INTEGRATIONS
  /mcp          → Manage MCP server connections.
  /hooks        → Configure automation hooks.
  /install-github-app → Connect Claude to GitHub PRs.
```

---

## 💡 Pro Tips for the Video

1. **Use `/clear` often** — Every new task = fresh context. Don't let old history eat your tokens.
2. **`/compact` before you hit the limit** — Don't wait for Claude to start forgetting. Compact proactively in long sessions.
3. **`/cost` after complex sessions** — Know what you're spending, especially on Opus.
4. **`/plan` for risky edits** — Always use Plan Mode before letting Claude touch production-critical files.
5. **`/help` is your truth** — Since Claude Code updates frequently, `/help` always shows the actual available commands for your installed version.
6. **Build custom commands for repeat tasks** — Any prompt you use more than 3 times should become a `/slash-command`.

---

## 🎯 Demo Flow Suggestion (for the Episode)

```
1. Open Task Manager project from /init episode
2. Show /help → point out all built-in commands
3. Demo /clear → show fresh context
4. Have a long conversation, then demo /compact
5. Show /cost → explain token awareness
6. Demo /plan → add a new feature with plan mode
7. Demo /undo → revert a change live on camera
8. Create a custom /review-task command live
9. Tease next episode → MCP integrations
```

---

*Reference: Anthropic Claude Code Official Docs — https://code.claude.com/docs*  
*Series: Aviz Academy — Claude Code for Developers*
