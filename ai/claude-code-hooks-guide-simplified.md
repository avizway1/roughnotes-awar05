# 🪝 Claude Code Hooks — Simple Guide

---

## What is a Hook?

A hook is a way to say:

> "Every time Claude does THIS → automatically do THAT."

No need to remind Claude. No need to type it every time. It just happens.

---

## Why Use Hooks?

Without hooks → you *hope* Claude remembers your rules.
With hooks → it's **guaranteed**, every single time.

---

## How a Hook Works

Every hook has 3 simple parts:

```
WHEN?        →    WHICH TOOL?    →    DO WHAT?
(Event)           (Matcher)           (Command)
```

**Example:**
```
After Claude edits a file  →  any Edit/Write  →  auto-format with Prettier
```

---

## Where Do Hooks Live?

All hooks are configured in one file:

```
.claude/settings.json
```

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Write|Edit",
        "hooks": [
          {
            "type": "command",
            "command": "npx prettier --write \"$CLAUDE_TOOL_INPUT_FILE_PATH\""
          }
        ]
      }
    ]
  }
}
```

---

## Common Hook Events

| Event | When It Fires |
|---|---|
| `SessionStart` | When you open a Claude Code session |
| `PreToolUse` | Just BEFORE Claude runs a tool |
| `PostToolUse` | Just AFTER Claude runs a tool |
| `Notification` | When Claude needs YOUR input / approval |
| `Stop` | When Claude finishes its task |
| `SessionEnd` | When the session closes |

---

## 3 Real-World Hook Examples

### 1. Auto-format code after Claude edits a file

```json
{
  "hooks": {
    "PostToolUse": [
      {
        "matcher": "Write|Edit",
        "hooks": [
          {
            "type": "command",
            "command": "npx prettier --write \"$CLAUDE_TOOL_INPUT_FILE_PATH\""
          }
        ]
      }
    ]
  }
}
```

---

### 2. Block dangerous shell commands

```json
{
  "hooks": {
    "PreToolUse": [
      {
        "matcher": "Bash",
        "hooks": [
          {
            "type": "command",
            "command": "echo \"$CLAUDE_TOOL_INPUT\" | grep -qE 'rm -rf|DROP TABLE' && exit 2 || exit 0"
          }
        ]
      }
    ]
  }
}
```

> `exit 2` tells Claude Code to **block** the action and stop.

---

### 3. Play a sound when Claude needs your approval

```json
{
  "hooks": {
    "Notification": [
      {
        "matcher": "",
        "hooks": [
          {
            "type": "command",
            "command": ".claude/hooks/notify-sound.sh"
          }
        ]
      }
    ]
  }
}
```

The shell script `.claude/hooks/notify-sound.sh`:

```bash
#!/bin/bash
afplay "$CLAUDE_PROJECT_DIR/templates/faaah.mp3"   # macOS
# paplay "$CLAUDE_PROJECT_DIR/templates/faaah.mp3" # Linux
exit 0
```

---

## Prompt to Ask Claude Code to Add the Sound Hook

Just paste this into Claude Code:

```
Create a hook that plays templates/faaah.mp3 whenever you need 
my input or approval.

Use the Notification event with an empty matcher.
Create the script at .claude/hooks/notify-sound.sh
Use $CLAUDE_PROJECT_DIR/templates/faaah.mp3 as the audio path.
Make it executable and register it in .claude/settings.json.
```

---

## Exit Codes — Quick Reference

| Exit Code | Meaning |
|---|---|
| `0` | All good — continue |
| `2` | Block the action — stop Claude |

---

## Key Takeaway

```
Hooks = Guaranteed automation at the right moment in Claude's workflow.
No reminders. No hoping. Just works.
```

---

*Aviz Academy — Claude Code Video Series*
