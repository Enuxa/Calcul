# Calcul

**A faire :** 
 - Classe `Contexte` correspondant aux variables libres et aux variables liées

##Description de l'algorithme récursif
Pour une expression *E*, après l'avoir décomposée en tokens et supprimé les parenthèses extérieures inutiles, on parcourt les tokens de droite à gauche jusqu'à trouver un opérateur :

 - hors parenthèses
 
 - qui n'admette pas d'opérateurs de priorité strictement inférieure à sa gauche (_ex :_ dans `7+2*12*(2*(7+2))` un tel opérateur est le premier `+`, dans `7+2-12*(2*(7+2))` il s'agit du `-` et dans `(2+5)*(4+2)*2^(9*(7-1))` c'est le premier `*`). Le fait qu'il n'admette pas d'opérateurs de priorité strictement inférieure à sa gauche permet de régler le problème de l'associativité, par exemple dans le cas de `1-1-1`, on ne peut pas la considérer comme `(1)-(1-1)` car la soustraction n'est pas associative, mais on peut effectivement la voir comme `(1-1)-(1)`, de même pour la division : `1/2/3` (bien que cette écriture soit ambiguë) elle se calcule de gauche à droite et vaut 1/6, pas 3/2

Ce qui nous laisse face à deux cas possibles :

 - Cet opérateur existe et on a `E = A°B`, où `°` est l'opérateur trouvé. On créé ainsi le nœud correspondant avec comme valeur cet opérateur, comme sous arbre gauche l'arbre construit à partir de `A` et  comme sous-arbre droit celui construit à partir de `B`.
(*Remarque :* si `A` est vide, c'est que l'opérateur `°` est unaire ou que l'expression est incorrecte)
 - Cet opérateur n'existe pas, c'est donc que `E` est une valeur "primitive" (*i.e.* une constante, une variable, ou en terme d'arbre : une feuille). Ce nœud est alors la feuille avec comme valeur celle décrite par `E`.

*Remarque :* Les *tokens* sont les éléments de bases de l'expression (les opérateurs, les nombres, les parenthèses, ...), par exemple la suite de tokens associée à l'expression `(1+2)-3^5/(7-2)` est `(,1,+,2,),-,3,^,5,/,(,7,-,2,)`.
