# Uruchomienie DietAnalyst w IntelliJ

1. Rozpakuj projekt.
2. W IntelliJ wybierz **File → Open** i wskaż folder projektu.
3. Poczekaj, aż IntelliJ zaimportuje zależności Maven.
4. Otwórz `src/main/java/pl/wat/dietanalyst/DietAnalystApplication.java`.
5. Kliknij zielony trójkąt przy metodzie `main()`.
6. Wejdź na `http://localhost:8080`.

Aplikacja domyślnie działa na bazie H2, więc Docker nie jest potrzebny.

W przypadku wcześniejszej wersji aplikacji baza H2 zostanie automatycznie zaktualizowana przez Hibernate. Gdyby stara baza powodowała konflikt, zamknij aplikację i usuń folder `data`, a następnie uruchom ją ponownie.
