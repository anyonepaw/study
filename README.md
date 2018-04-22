# Сокрытие QR-кода в картинке c помощью метода псевдослучайной перестановки. 
# Водный знак. 
---
Как доказать что вы - автор? Скрыть свой водный знак!
Как передать информацию так, чтобы никто не заметил? Поместить ее на самом видимом месте!

Берем фотографию (файл-контейнер): 

![Image alt](https://github.com/anyonepaw/study/blob/master/foxy.bmp)

Берем qr-код (скрываемое сообщение или цифровой водяной знак):

![Image alt](https://github.com/anyonepaw/study/blob/master/qr.bmp)

Когда вы откроете оба файла в программе, она будет выглядеть следующим образом:

![Image alt](https://github.com/anyonepaw/study/blob/master/GUI.png)

Введите ключ и желаемое количество раундов:) В результате, получим:

![Image alt](https://github.com/anyonepaw/study/blob/master/result.png)

Почему-то кажется, что ничего не изменилось.

Метод псевдослучайной перестановки заключается в рассеивании битов монохромной картинки по фотографии по специальному алгоритму, вставляя их на конец пикселей синего канала. Поэтому, на самом деле происходит следующее:

При количестве раундов R=2:

![Image alt](https://github.com/anyonepaw/study/blob/master/2.png)

При количестве раундов R=4:

![Image alt](https://github.com/anyonepaw/study/blob/master/4.png)

При количестве раундов R=6:

![Image alt](https://github.com/anyonepaw/study/blob/master/6.png)

При количестве раундов R=8:

![Image alt](https://github.com/anyonepaw/study/blob/master/8.png)

При количестве раундов R=12:

![Image alt](https://github.com/anyonepaw/study/blob/master/12.png)

Как мы можем видеть, желаемое количество раундов для этого метода - от 8 до 12. 

Теперь Вы обезопасили свои авторские права или передали нужную информацию:)

ps.

При расшифровке фотографии Вы должны будете знать ключ:З
