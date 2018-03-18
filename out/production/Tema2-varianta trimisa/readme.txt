Calinescu Cristina, 334CC
APD - Tema 2

	In implementarea temei am creat 2 clase pentru reprezentarea unei tabele, respectiv a unei coloane(Tabel, Column).
	In Database am urmatoarele metode:
-initDb : initializeaza numarul de thread-uri cu valoarea primita si creeaza un ExecutorService cu numarul maxim de threaduri egal cu cel primit.
-stopDb : opreste ExecutorService-ul creat anterior
-createTable : adaug in lista de tabele pe care o am ca membru in Database o noua tabela cu caracteristicile necesare
	Pentru a face select, update si insert blocante, am un semafor in Table care nu permite accesul mai multor thread-uri in acelasi timp; La fiecare intrare intr-una din operatii, blochez accesul la inceput si eliberez semaforul dupa ce fac operatiile necesare;
-select : dupa ce obtin tabela cu numele respectiv; Folosind metoda firstFilter elimin liniile care nu respecta conditia data. Pe fiecare din aceasta coloana calculez apoi operatia necesara folosind secondFilter;
-update : modific linia ce respecta conditia data; updateColumn verifica conditia si, atunci cand gaseste linia unde aceasta se respecta, parcurge fiecare coloana si insereaza noua valoare la indexul corespunzator;
-insert : parcurg aici lista de coloane si, la finalul fiecareia, adaug valoarea corespunzatoare;
-startTransaction : scad valoarea numarului de thread-uri ce au acces pe tabela curenta, folosind semaforul cu rol de mutex;
-endTransaction : cresc valoarea numarului de thread-uri ce au acces pe tabela curenta prin semaforul folosit si pentru startTransaction;
-getTable : returneaza, din lista de tabele alea bazei de date, tabela cu numele cerut;

	Pentru sanityCheck obtin rezultatele corecte.
	Pentru testConsistency obtin PASS la toate cele 3 verificari, iar apoi obtin o eroare de tipul "Exception in thread "Thread-1" java.lang.IndexOutOfBoundsException: Index 0 out-of-bounds for length 0" pe care nu am reusit sa o rezolv.
	Din cauza erorii anterioare testul de scalabilitate nu se mai realizeaza. Daca comentez in Main linia in care se apeleaza testConsistency, verificarea scalabilitatii se realizeaza.
	In ceea ce priveste scalabilitatea, rezultatele obtinute local pentru 100 000 thread-uri sunt:
- 1 thread :
		Insert time 329
		Update time 8
		Select time 168
- 2 thread-uri :
		Insert time 127
		Update time 3
		Select time 100
- 4 thread-uri :
		Insert time 118
		Update time 1
		Select time 67
 
	 