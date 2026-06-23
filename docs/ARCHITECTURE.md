# Architektura DietAnalyst

Aplikacja wykorzystuje podejście Boundary–Control–Entity obecne w modelach EA.

- **Boundary**: kontrolery MVC i formularze wejściowe.
- **Control**: klasy `*Manager` realizujące przypadki użycia.
- **Entity**: encje JPA `UserAccount`, `Meal`, `Product`, `DietPlan`, `AuditLog`.
- **Interfaces**: kontrakty `IAutoryzacja`, `IBazaProduktow`, `IZarzadzanieDziennikiem`, `IPlanDiety`, `IRaportowanie`.
- **Repository**: Spring Data JPA i baza MySQL.
- **Security**: uwierzytelnianie formularzowe i role USER/DIETITIAN/ADMIN.

## Główne przepływy

1. Rejestracja tworzy wyłącznie konto USER.
2. Administrator może nadać rolę, aktywować konto i przypisać dietetyka.
3. Użytkownik prowadzi własny dziennik i generuje plan.
4. Dietetyk widzi tylko przypisanych użytkowników i recenzuje ich plany.
5. Administrator ma pełny wgląd w konta oraz dziennik zdarzeń.
