# 🎬 Video Reference: Session & Context Management in Claude Code
**Series:** Aviz Academy — Claude Code
**Topic:** Session & Context Management
**Docs:** [Context Windows](https://platform.claude.com/docs/en/build-with-claude/context-windows) | [Token Counting](https://platform.claude.com/docs/en/build-with-claude/token-counting)

---

## 🧠 Analogy to Open With (Hook)

> "Think of the context window like a **whiteboard in a meeting room**.
> You can only write so much before it gets full.
> Claude can only 'remember' what's on that whiteboard right now — nothing more, nothing less."

Use this analogy throughout — it maps cleanly to every concept in this video.

---

## 📌 Section 1: What Is a Context Window?

**Key Point:** The context window is Claude's "working memory" — not its training data.

- Claude was trained on a huge corpus (like reading all the books in a library)
- But when you start a conversation, Claude only "sees" what's in the current session
- That active session space = the **context window**

**Whiteboard analogy:**
- Training = all the knowledge in Claude's head
- Context window = what's written on the whiteboard right now
- Once the whiteboard is full, old content must be erased to make room

**Key technical facts to mention:**
- Context window can hold **up to 1 million tokens** (on supported models)
- Every request includes: system prompt + full conversation history + your new message + the response
- Tokens grow **linearly** — every turn adds more to the whiteboard

---

## 📌 Section 2: What Is a Token? (Quick Explainer)

**Key Point:** Tokens are chunks of text — not exactly words, not exactly characters.

- Rule of thumb: **~1 token ≈ 4 characters** or **~¾ of a word**
- "Hello, how are you?" ≈ ~5 tokens
- A 1,000-word document ≈ ~1,300–1,500 tokens

**Why it matters:**
- Tokens = cost (you're billed per token)
- Tokens = capacity (you have a max limit per request)
- More tokens in = slower response, more cost

**Quick mental model for students:**
> "Tokens are like tiles in a Scrabble game. Each word costs you some tiles. You only have so many tiles on your rack."

---

## 📌 Section 3: How the Context Fills Up Over Time

**Diagram to draw/show on screen:**

```
Turn 1:  [System Prompt] + [User Msg 1] → [Response 1]
Turn 2:  [System Prompt] + [User Msg 1] + [Response 1] + [User Msg 2] → [Response 2]
Turn 3:  [System Prompt] + [All Previous] + [User Msg 3] → [Response 3]
          ↑ Keeps growing with every turn
```

**Key teaching point:**
- Claude API does NOT have memory between sessions
- Within a session, you must send the full conversation history each time
- The window keeps growing → eventually hits the limit

**Whiteboard analogy:**
> "Every time you message Claude, you're rewriting the entire whiteboard from scratch — everything that was said before, plus your new message."

---

## 📌 Section 4: What Is "Context Rot"?

**Key Point:** More context isn't always better.

- As the context fills up, Claude's **accuracy and recall start to degrade**
- This is called **context rot**
- It's like adding too many notes on a whiteboard — hard to find what matters

**Anthropic's benchmark insight to mention:**
- Claude performs well on long-context benchmarks (MRCR, GraphWalks)
- But performance still depends on *what's* in context, not just *how much* fits
- **Quality > Quantity** — curate your context!

---

## 📌 Section 5: Context Window + Extended Thinking (Brief Mention)

> *(Optional — mention only if targeting intermediate viewers)*

**Key Point:** When extended thinking is ON, thinking tokens are auto-stripped from history.

- Extended thinking = Claude "thinks out loud" before answering
- Those thinking blocks are large → could eat up the window fast
- Anthropic automatically removes previous thinking blocks from context
- You only pay for them **once**, when they're generated

**Whiteboard analogy:**
> "Claude's rough notes (thinking) get erased after each turn. Only the final answer stays on the whiteboard."

---

## 📌 Section 6: Token Counting — Why It Matters

**Key Point:** You can count tokens *before* sending a request — without burning a full API call.

**Use cases to highlight:**
| Situation | Why Count First? |
|---|---|
| Long document analysis | Avoid hitting limits mid-conversation |
| Building a chatbot | Control costs proactively |
| Rate limit management | Stay under quota thresholds |
| Prompt optimization | Trim prompts without guessing |
| Smart model routing | Short prompt → cheaper model; Long → powerful model |

---

## 📌 Section 7: Strategies to Manage Context (The Practical Bit)

**Frame this as "what you can actually DO about it":**

### Strategy 1 — Compaction (Primary Strategy)
- For long-running conversations and agentic workflows
- Claude summarizes older parts of the conversation to free up space
- Like **erasing old whiteboard notes** and replacing with a summary
- Docs: [Compaction guide](https://platform.claude.com/docs/en/build-with-claude/compaction)

### Strategy 2 — Context Editing
- More surgical: clear specific parts (e.g., old tool results, thinking blocks)
- Like erasing **one section** of the whiteboard instead of all of it
- Docs: [Context editing guide](https://platform.claude.com/docs/en/build-with-claude/context-editing)

---

## 📌 Section 8: Token Count Caveats (Mention Briefly)

- The token count from `/count_tokens` is an **estimate**, not exact
- Actual usage during a real API call may vary slightly
- Some tokens are added automatically by Anthropic (system optimizations) — **you are NOT billed for those**
- Billing = only your content tokens

---

## 🎯 Key Takeaways to Land at the End

1. **Context window = Claude's whiteboard** — limited, temporary, grows with every turn
2. **Tokens = cost + capacity** — always know how many you're using
3. **More context ≠ better** — context rot is real, curate what goes in
4. **Count before you send** — use `/count_tokens` to plan ahead
5. **You have tools to manage it** — compaction, editing, caching

---

*Reference prepared for Aviz Academy | Claude Code Series*
