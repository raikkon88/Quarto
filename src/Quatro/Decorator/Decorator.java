package Quatro.Decorator;

/**
 * @Author : Marc Sànchez Pifarré
 * Udg Code : u1939705
 * Classe Decorator
 * ------------------------------
 * és l'objecte base que conté el node decorat. Es podria haver prescindit d'ell peró s'ha realitzat seguint el patró decorator estudiat a ESII.
 */
public abstract class Decorator extends Node {

    // Objecte decorat
    protected Node decorated;

}
