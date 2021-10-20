# MexiApp
Useful desktop app to help a user make an appointment on the [mexitel website](https://mexitel.sre.gob.mx/citas.webportal/pages/public/login/login.jsf)

## Problem to solve
To make an appointment on the [mexitel website](https://mexitel.sre.gob.mx/citas.webportal/pages/public/login/login.jsf) and conclude the process, it sends a pdf file to the user's email that contains a code and a token, which the user must use to validate the appointment. The problem is that the code is embedded in an image and the token is very long, and the [mexitel website](https://mexitel.sre.gob.mx/citas.webportal/pages/public/login/login.jsf) gives a very short time limit for the user to enter them, when doing it manually takes a long time.

## Features
- Speeds up the process of downloading and reading the pdf file.
- Allows to get the code and token from the clipboard.

## Note
Works with Gmail only, if you want that work with other email server modify the ```Mail.java``` file.

## Example
**PDF:**
![PDF Image](captures/image_01.png)

**Result:**
![App Image](captures/image_02.png)
