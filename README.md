# F1
Aplikacja do wyciągania danych z forum [Bankier.pl](https://www.bankier.pl/forum). 
Aplikacja została stworzona w celu wydobycia danych, na którch można dokonać wpływu opini pubicznej na wachania kwoty akcji spółki.

## Funkcjonalności
1. Wydobywanie danych do ~1.5KK rokordów na dzień
2. Generowanie raportów

## Sposób działania
Aplikacja jest obsługiwana przez protokół HTTP(przykładowa dokumentacja do Postmana dostęna w folderze `.readme`), po wysłaniu żądania, wiadomość jest odkładana na Kafkę.
Wiadomości z kafki są obsugiwane poprzez pobranie stony oraz następnie jej analizę, następnie zapisanie rekordu od bazy oraz alternatywnie zapisywanie kolejnych wiadomości do Kafki.

## Wymagania
1. JDK 17
1. Docker
1. Postman
