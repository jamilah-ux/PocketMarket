# Architecture Decision Records (ADR) - PocketMarket

## ADR 01: MVVM with Unidirectional Data Flow (UDF)
* **Status**: Accepted
* **Context**: Needed a scalable UI architecture for Jetpack Compose ensuring predictable state transitions.
* **Decision**: Adopted MVVM pattern where ViewModels expose `StateFlow` for UI state and `Channel`/`SharedFlow` for one-time `UiEffect` events.
* **Consequences**: Clear separation of concerns, testable business logic, and UI elements remain purely declarative.

## ADR 02: Dependency Injection with Dagger Hilt
* **Status**: Accepted
* **Context**: Managing instances for Room Database, DataStore, and Retrofit network services manually introduces tight coupling.
* **Decision**: Utilized Hilt for dependency injection across ViewModels, Repositories, and Data Sources.
* **Consequences**: Simplified unit testing via mock replacements and compile-time dependency validation.

## ADR 03: Room & Paging 3 Offline First Architecture
* **Status**: Accepted
* **Context**: App must remain functional in offline scenarios with graceful fallback.
* **Decision**: Implemented Room Persistent DB combined with Paging 3 library for seamless offline caching and paginated catalog fetching.