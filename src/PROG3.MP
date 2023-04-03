PROGRAMME test_condition;
VAR resultat;
DEBUT
    SI resultat ALORS
    DEBUT
        ECRIRE('Le résultat est différent de zéro');
        ECRIRE();
        ECRIRE('Il est égal à:', resultat)
    FIN;
    SINON
        ECRIRE('Le résultat est null')
FIN.