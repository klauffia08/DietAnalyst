# DietAnalyst – aplikacja wielorólowa

Pełna aplikacja Spring Boot zgodna z modelem BCE zastosowanym w projekcie PWST.

## Role i odpowiedzialności

### Użytkownik
- prowadzi własny dziennik posiłków,
- przegląda wspólną bazę produktów,
- generuje i wysyła własne plany do dietetyka,
- przegląda własne raporty i profil.

### Dietetyk
- nie posiada funkcji dziennika użytkownika,
- widzi wyłącznie przypisanych podopiecznych,
- analizuje ich profile, posiłki i bieżące postępy,
- zatwierdza albo odrzuca przesłane plany,
- zarządza bazą produktów.

### Administrator
- nie posiada funkcji użytkownika ani dietetyka,
- zarządza kontami, rolami, aktywnością i przypisaniami,
- przegląda statystyki systemu i dziennik audytowy,
- zarządza bazą produktów.

## Uruchomienie w IntelliJ

1. Otwórz katalog projektu jako projekt Maven.
2. Uruchom klasę `pl.wat.dietanalyst.DietAnalystApplication`.
3. Otwórz `http://localhost:8080`.

Domyślnie używany jest profil H2 i trwała baza plikowa `data/dietanalyst.mv.db`. Docker nie jest wymagany.

## Konta demonstracyjne

| Rola | Login | Hasło |
|---|---|---|
| Administrator | `admin@dietanalyst.pl` | `Admin123!` |
| Dietetyk | `dietetyk@dietanalyst.pl` | `Diet123!` |
| Użytkownik | `user@dietanalyst.pl` | `User123!` |

Nowe konta rejestrowane publicznie otrzymują zawsze rolę `USER`. Role uprzywilejowane nadaje administrator.

## Baza H2

Konsola: `http://localhost:8080/h2-console`

- JDBC URL: `jdbc:h2:file:./data/dietanalyst`
- użytkownik: `sa`
- hasło: puste

## MySQL opcjonalnie

Profil MySQL można uruchomić argumentem:

```text
--spring.profiles.active=mysql
```

Wtedy wymagany jest działający serwer MySQL albo Docker Compose.
