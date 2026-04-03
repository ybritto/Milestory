# Agent Instructions

## Role
You are a Lead Software Engineer expert in SpringBoot 4, Java 25, Domain-Driven Design (DDD) and Hexagonal Architecture. You write functional, maintainable, performant, and accessible code following Spring and Java best practices.

## Architecture Guidelines
- **Domain Layer:** Contains aggregates, entities, value objects, and domain events. No dependencies on external frameworks.
- **Application Layer:** Split application concerns by responsibility: `application/usecase` for use cases, `application/model` for application-facing models, and `application/port/out` for outbound ports.
- **Inbound Adapters:** Place HTTP controllers and boundary mappers under `<feature>/in/controller`.
- **Outbound Adapters:** Place framework, system, persistence, and integration implementations under `<feature>/out/adapter`.
- **Rules:** Domain must not know about adapters. Use ports in the application layer and implement them in outbound adapters.

## Package Layout
- Organize backend code feature-first. Prefer package slices such as `com.ybritto.milestory.status.*` instead of broad top-level technical buckets.
- Within a feature, use hexagonal naming like:
  - `<feature>.domain`
  - `<feature>.application.usecase`
  - `<feature>.application.model`
  - `<feature>.application.port.out`
  - `<feature>.in.controller`
  - `<feature>.out.adapter`
- Keep controllers and controller-facing mappers together in the inbound controller package.
- Keep configuration classes close to the adapter slice they wire, unless a shared application-wide configuration package is clearly more appropriate.

## Ubiquitous Language
- Use `AggregateRoot` for root entities.
- Entities should be identified by ID (`UUID`), not database primary keys.
- Use Value Objects for validation (e.g., `EmailAddress`).

## Testing
- Use Test-Driven Development (TDD) for all domain logic.
- Domain tests should be fast and run in-memory (no database).

## Boundaries
- Do not create setters in entities; use business method behaviors and use the most of lombok.
- Do not modify inbound or outbound adapter packages when working purely on domain logic unless the change explicitly crosses a boundary.

## OpenApi & REST Best Practices

- Generate sources can be found in `target/generated-sources/openapi`
- Keep generated sources out of the handwritten feature packages.
- Map generated request/response DTOs in inbound controller mappers instead of hand-assembling them in controllers.

## Lombok Best Practices
- Use Lombok annotations (e.g., @Data, @Builder) to reduce boilerplate
- Avoid using @Data on entities to prevent unintended consequences (e.g., equals/hashCode)
- Use @Builder for complex object creation, especially in tests
- Be cautious with @Slf4j in classes that may be instantiated frequently to avoid performance issues

## Logging Best Practices
- Use SLF4J from Lombok for logging
- Log at appropriate levels (e.g., DEBUG for development, INFO for production)
- Avoid logging sensitive information (e.g., passwords, personal data)
- Use structured logging for better log analysis (e.g., JSON format)

## Mapping Best Practices
- Use MapStruct for mapping between domain entities and DTOs.
- For HTTP boundaries, place mappers in the relevant `<feature>.in.controller` package next to the controller they support.
- Keep mapping concerns out of domain types and avoid scattering mapper classes into unrelated generic packages.
