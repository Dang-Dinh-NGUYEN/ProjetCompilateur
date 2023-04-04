PROGRAMME test_condition;
VAR resultat;
DEBUT
    ECRIRE('Entrez un nombre entier: ');
    LIRE(resultat);
    SI resultat ALORS
    DEBUT
        ECRIRE('Le résultat est différent de zéro');
        ECRIRE();
        ECRIRE('Il est égal à:', resultat)
    FIN;
    SINON
        ECRIRE('Le résultat est null')
FIN.