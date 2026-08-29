# Reflections on AI-Assisted Software Engineering

This project uses an LLM as an implementation partner. The process is
structured around small releases, explicit requirements, automated tests, and
user review before commits.

## Interesting prompts

### 1. Product brief and constraints

The initial request combined the target users, transaction and budgeting
features, Java SE 25, Gradle, JavaFX, offline operation, documentation, tests,
and cross-platform support. This was interesting because it defined both the
product behavior and the engineering constraints in one prompt. The useful
effect was to establish a traceable baseline for architecture and acceptance
criteria instead of starting with an isolated screen.

### 2. Project-manager clarification

The request to ask for more details before starting encouraged requirement
discovery rather than premature implementation. The clarification questions
covered users, feature scope, UI toolkit, persistence, platforms, and testing.
The user then resolved the highest-impact choices: JavaFX, in-memory data,
offline use, and support for macOS, Windows, and Linux.

### 3. Incremental implementation and review checkpoints

The instruction to implement step by step and ask the user to check and commit
after each step changes the interaction from one large code generation task into
a reviewable software-engineering process. It makes each release easier to
inspect, keeps documentation synchronized, and gives the user control over
when changes become committed history.


