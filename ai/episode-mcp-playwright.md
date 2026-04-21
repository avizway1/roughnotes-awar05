# Episode: MCP in Claude Code — Playwright Live Demo
### Aviz Academy — Claude Code for Developers

---

## PART 1 — What is MCP? (On-Camera Intro)

### The One-Line Explanation

> MCP (Model Context Protocol) is an open standard that lets Claude Code connect to external tools and services — like a plugin system for your AI.

### The Analogy to Use On Camera

Without MCP, Claude Code works only inside your project folder — like a brilliant developer locked in a room.

**MCP opens the doors and windows.** Claude can now reach outside — browse the web, control a browser, query a database — all from a single natural language prompt.

### How MCP Works (The Architecture)

```
Your Prompt (natural language)
        │
        ▼
  Claude Code (AI brain)
        │
        │  speaks via MCP protocol
        ▼
  MCP Servers (the connectors)
        │
        ├── Playwright MCP  →  Controls a real browser  ← today's episode
        ├── GitHub MCP      →  Reads PRs, creates issues
        ├── SQLite MCP      →  Queries your database
        ├── Slack MCP       →  Posts messages & files
        └── (any custom MCP you build)
```

### Two Types of MCP Servers

| Type | What it means | Example |
|---|---|---|
| **Local (stdio)** | Runs on your machine via `npx` | Playwright MCP |
| **Remote (http)** | Hosted server, connects via URL + OAuth | GitHub MCP |

---

## PART 2 — What is Playwright? (Background for Viewers)

### The Origin

Playwright is an **open-source browser automation framework** built and maintained by **Microsoft**. It was released in 2020 as a modern successor to older tools like Selenium and Puppeteer.

It is written in TypeScript and supports all three major browser engines out of the box — Chromium (Chrome/Edge), Firefox, and WebKit (Safari) — from a single API.

### What Developers Use it For

Traditionally, developers write Playwright **scripts in code** to automate browser tasks:

```typescript
// Traditional Playwright — you write all of this manually
const { chromium } = require('playwright');
const browser = await chromium.launch();
const page = await browser.newPage();
await page.goto('http://localhost:5000');
await page.fill('input[name="title"]', 'Deploy to production');
await page.click('button[type="submit"]');
await page.screenshot({ path: 'screenshots/task-added.png' });
await browser.close();
```

This is powerful but requires you to know the selectors, write the code, debug it, and maintain it.

### What Changes with MCP

With the **Playwright MCP server**, Claude Code gets direct access to Playwright's browser controls — **no scripting needed**. You describe what you want in plain English and Claude handles the rest:

```
"Open the Task Manager app, add a task, take a screenshot and save it"
```

That one sentence replaces the entire script above.

### Why Playwright Over Other Tools

| Feature | Playwright | Selenium | Puppeteer |
|---|---|---|---|
| Maintained by | Microsoft | Open-source community | Google |
| Browser support | Chromium, Firefox, WebKit | All major browsers | Chromium only |
| Speed | Fast | Slower | Fast |
| Modern API | Yes | Legacy feel | Yes |
| MCP server available | ✅ Official | ❌ No | ❌ No |

### The Playwright MCP Server

The MCP server used in this episode is the **official `@playwright/mcp` package** — maintained by Microsoft, not a third-party tool. This matters because it is actively updated alongside Playwright itself and works directly with Claude Code's MCP protocol.

---

## PART 3 — Prerequisites (Show on Screen)

Before the demo, make sure you have:

```bash
# Check Node.js version (must be 18+)
node --version

# Check Claude Code is installed
claude --version

# Your Task Manager app must be running
python3 app.py
# App should be live at http://localhost:5000
```

Also create the screenshots folder in your project:

```bash
mkdir -p screenshots
```

> Add `screenshots/` to your `.gitignore` so test screenshots don't get committed to version control.

```bash
echo "screenshots/" >> .gitignore
```

---

## PART 4 — Adding Playwright MCP

### Step 1 — Register Playwright MCP (one command)

Run this inside your project directory:

```bash
claude mcp add playwright npx @playwright/mcp@latest
```

> **What this does:** Registers the official Microsoft Playwright MCP server for this project. The configuration is saved automatically to `~/.claude.json` under your project path.

For team-wide sharing (saves to `.mcp.json` in version control):

```bash
claude mcp add --scope project playwright npx @playwright/mcp@latest
```

### Step 2 — Verify it was added

```bash
claude mcp list
```

You should see:

```
playwright: npx @playwright/mcp@latest  (connected)
```

### What Playwright MCP Gives Claude

| Tool | What Claude can do |
|---|---|
| `browser_navigate` | Open any URL in a real browser |
| `browser_click` | Click buttons, links, form elements |
| `browser_type` | Fill in text fields |
| `browser_take_screenshot` | Capture the current page as a PNG |
| `browser_snapshot` | Read the page structure (accessibility tree) |
| `browser_wait_for` | Wait for elements to appear before acting |
| `browser_save_storage_state` | Save login session for reuse |

---

## PART 5 — Verifying MCP Settings (Show These on Camera)

### Where the config lives

Claude Code stores MCP config in these locations:

| Location | Scope | When to use |
|---|---|---|
| `~/.claude.json` | Per-project (local only) | Default — `claude mcp add` with no flag |
| `.mcp.json` (project root) | Team-shared via git | Use `--scope project` flag |
| `~/.claude/settings.json` | Global across all projects | Use `--scope user` flag |

### Check config inside a Claude Code session

Once inside Claude Code, run:

```
/mcp
```

This shows all connected MCP servers and their available tools. Navigate to `playwright` to see the full tool list — great to show on camera so viewers can see exactly what Claude has access to.

### Manual config check (show in terminal on camera)

```bash
cat ~/.claude.json | grep -A 10 "mcpServers"
```

Expected output:

```json
{
  "mcpServers": {
    "playwright": {
      "command": "npx",
      "args": ["@playwright/mcp@latest"]
    }
  }
}
```

---

## PART 6 — The Live Demo 🎬

### Pre-Demo Checklist

- [ ] Task Manager app running at `http://localhost:5000`
- [ ] `screenshots/` folder exists in the project directory
- [ ] Claude Code session open in the `task-management/` directory
- [ ] Playwright MCP showing as connected via `/mcp`
- [ ] Terminal and browser both visible on screen

---

### The Demo Prompt (copy-paste this on camera)

```
Use playwright MCP to open the Task Manager app at http://localhost:5000,
add a new task called "Deploy to production 🚀", wait for the task to 
appear in the list, then take a screenshot and save it to the 
screenshots/ folder in the project directory as task-added.png
```

---

### What the Audience Will See (Step by Step)

| Step | What happens on screen |
|---|---|
| 1 | A Chrome browser window opens automatically |
| 2 | Claude navigates to `http://localhost:5000` |
| 3 | Claude locates the input field and types the task title |
| 4 | Claude clicks the "Add Task" button |
| 5 | The new task appears in the task list |
| 6 | Claude takes a screenshot of the page |
| 7 | The file `screenshots/task-added.png` appears in the project folder |
| 8 | You open the screenshot live on camera to show the result |

---

### Pro Tips for On-Camera Delivery

> **Always say "use playwright MCP"** in your first prompt of the session. Without this, Claude sometimes defaults to running Playwright via Bash commands instead of through the MCP server — a common gotcha flagged by the Playwright team.

> **Run the app first** — confirm `localhost:5000` is loading in your browser before starting the Claude Code session. Nothing kills a live demo like the app not being up.

> **Show the screenshots/ folder** in your file explorer before the demo starts — it should be empty. After the demo, open it live to reveal the saved screenshot. This before-and-after moment lands really well on camera.

---

## PART 7 — Closing Talking Points

### What just happened (summarise for viewers)

- Claude received **one natural language prompt**
- It used the **Playwright MCP server** to control a real browser
- No Playwright scripts. No manual clicks. No selectors written by hand.
- The screenshot landed in the project folder automatically
- This is what **agentic AI with MCP** looks like in practice

### What Playwright MCP unlocks for real teams

| Use case | What Claude does |
|---|---|
| Self-QA after code changes | Navigates the app, checks for visual regressions |
| Automated form testing | Fills forms, submits, verifies responses |
| Screenshot documentation | Captures UI states for changelogs or PRs |
| Exploratory testing | Walks through flows in plain English, reports issues |
| Authenticated testing | Log in manually once, Claude reuses the session |

### Tease the next episode

> "Now that you've seen how MCP connects Claude to external tools, in the next episode we'll go one level deeper — we'll build a **custom MCP server from scratch**, so Claude can talk to any tool or API you want, not just the ones that already have a package."

---

## Quick Reference Card

```bash
# Add Playwright MCP to your project
claude mcp add playwright npx @playwright/mcp@latest

# Add with project scope (shared with team via .mcp.json)
claude mcp add --scope project playwright npx @playwright/mcp@latest

# List all connected MCP servers
claude mcp list

# Verify tools inside a Claude Code session
/mcp

# Create screenshots folder
mkdir -p screenshots && echo "screenshots/" >> .gitignore

# The demo prompt
# "Use playwright MCP to open the Task Manager app at http://localhost:5000,
#  add a new task called 'Deploy to production 🚀', wait for the task to
#  appear in the list, then take a screenshot and save it to the
#  screenshots/ folder as task-added.png"
```

---

*Aviz Academy — Claude Code for Developers | Episode: MCP Integrations*
