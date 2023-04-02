PROGRAMME exemple;
VAR nb;
DEBUT
    {{commentaires}}
    ECRIRE('Entrez un nombre entier: ');
    LIRE(nb);
    nb := 2*nb;
    ECRIRE();
    ECRIRE('Le double de ce nombre est: ', nb)
FIN.