# ReedSolomonProject

Dieses Projekt dient der Codierung und Decodierung von Reed-Solomon-Codes und wurde im Rahmen einer Masterarbeit angefertigt. Es nutzt das MVC-Pattern und hat eine Swing GUI, mit der Nutzer interagieren können. 
Der Nutzer hat die Möglichkeit Nachrichten oder Codes einzugeben und diese zu codieren bzw. decodieren. Über verschiedene Eingabefelder kann die Größe des gewählten endlichen Körpers und entsprechend der verwendete RS(n,k,d)-Code festgelegt werden. Ein Dropdown-Menü bietet voreingestellte Werte, die in der Praxis eingesetzt werden und mit Hilfe eines Random-Buttons können zufällige Nachrichten erzeugt werden. Es kann unterschieden werden zwischen stützstellenbasierter und systematischer Codierung. Bei der Decodierung wurden die beiden Verfahren Berlekamp-Welch und Berlekamp-Massey implementiert. Außerdem werden bei der Ausführung die Laufzeiten gemessen und mit ausgegeben. Auf der rechten Seite der Benutzeroberfläche werden Logs mit einigen Zwischenschritten ausgegeben. 

### Setup
Das Projekt kann über Git ausgecheckt und in einer Java-Entwicklerumgebung, wie z.B. Eclipse oder IntelliJ, geöffnet werden. Über die Main-Methode kann es direkt gestartet werden. Dabei wird die Benutzeroberfläche angezeigt und man hat direkt die Möglichkeit das Tool zu nutzen. 
Zur alternativen Ausführung liegt eine exportierte Jar-Datei bei, wobei empfohlen wird, das Programm in einer Entwicklungsumgebung zu öffnen, um tiefere Einblicke in die Auführung zu erhalten.

### Zu Beachten 
Das primitive Polynom muss als Dezimalwert angegeben werden.
Bsp.: Für das Polynom *p(x)=x^3+x^2+1* beim GF(2^3) muss der Wert *13* angegeben werden.

Einige Beispielwert, mit denen das Programm getestet und Laufzeiten gemessen wurden:
* GF(2^3) mit RS(7,3,4) und p(x)=x^3+x^2+1=13
* GF(2^5) mit RS(31,15,16) und p(x)=x^5+x^2+1=37
* GF(2^7) mit RS(127,101,26) und p(x)=x^7+x+1=131
* GF(2^8) mit RS(255,233,32) und p(x)=x^8+x^4+x^3+x+1=285
