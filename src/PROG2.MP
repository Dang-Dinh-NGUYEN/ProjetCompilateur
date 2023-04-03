PROGRAMME test_loop;
VAR i;
DEBUT
    i := 10;
    TANTQUE i FAIRE
    DEBUT
        ECRIRE('',i);
        i := i - 1
    FIN;
    ECRIRE('0');
    ECRIRE()
FIN.