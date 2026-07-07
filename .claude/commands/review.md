---
description: Spawn the code-reviewer sub-agent to review the current changes
---

Use the **code-reviewer** subagent to review the code changes on the current
branch.

$ARGUMENTS

Instructions for the subagent:
- Diff the current branch against `main` (`git diff main...HEAD`) unless the
  arguments above specify a different target.
- Focus on correctness, Kotlin/Gradle best practices, and this project's
  conventions (small functions, DDD, delegation to associated objects).
- Report findings most-severe first, with `file:line` references.