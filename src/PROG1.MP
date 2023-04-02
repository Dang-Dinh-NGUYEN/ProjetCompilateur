PROGRAMME exemple;
VAR nb;
DEBUT
    {{commentaires}}
    ECRIRE('entrez un nombre entier: ');
    LIRE(nb);
    nb := nb*nb;
    ECRIRE();
    ECRIRE('le double de ce nombre est: ', nb)
FIN
.