reversi-android
===============

A port of my Reversi / Othello game to the Android platform. (not yet fun!)

You can build and run it yourself in Eclipse. Just ensure you've got the
latest APIs, then in your workspace select File > Import and pick the
driectory you've git cloned this repo into, then hit "play"!

July 13, 2013: First actually-playable version!
* Fixed view layout & background issues
* Added 300ms pause before computer turn
* Correctly handle end-game

July 23, 2013:
* Fixed issue with the AI moving first on the second game.

July 25, 2013:
* Actually displays end-of-game alert now.
* Game is fun to play!
* Reduced lookahead to 3 from 5 for playability on older devices

July 26, 2013:
* Published! https://play.google.com/store/apps/details?id=ly.dweek.reversi

IMMEDIATE TODOs:
* Add Game Over screen that displays score before starting a new game
* Add 'Resign' button
* Import audio effects from iOS client
* Import icons/graphics from iOS client
* Allow user to select difficulty level

BIGGER TODOs:
* Include other A.I.s?
* Add two-player local-human gameplay
* Facebook login, Parse backend for challenging friends
* Send moves to server to validate fair matches & keep rankings
