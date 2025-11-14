package dev.agiro.matriarch.suppliers.personal;

import dev.agiro.matriarch.suppliers.base.RandomSupplier;

/**
 * Supplier that generates random first names.
 */
public class FirstNameSupplier extends RandomSupplier<String> {

    private static final String[] FIRST_NAMES = {
            "James", "Mary", "John", "Patricia", "Robert", "Jennifer", "Michael", "Linda",
            "William", "Barbara", "David", "Elizabeth", "Richard", "Susan", "Joseph", "Jessica",
            "Thomas", "Sarah", "Charles", "Karen", "Christopher", "Nancy", "Daniel", "Lisa",
            "Matthew", "Betty", "Anthony", "Margaret", "Mark", "Sandra", "Donald", "Ashley",
            "Steven", "Kimberly", "Paul", "Emily", "Andrew", "Donna", "Joshua", "Michelle",
            "Kenneth", "Dorothy", "Kevin", "Carol", "Brian", "Amanda", "George", "Melissa",
            "Edward", "Deborah", "Ronald", "Stephanie", "Timothy", "Rebecca", "Jason", "Sharon",
            "Jeffrey", "Laura", "Ryan", "Cynthia", "Jacob", "Kathleen", "Gary", "Amy",
            "Nicholas", "Shirley", "Eric", "Angela", "Jonathan", "Helen", "Stephen", "Anna",
            "Larry", "Brenda", "Justin", "Pamela", "Scott", "Nicole", "Brandon", "Emma",
            "Albert", "Sara", "Marina", "Estela", "Jordi", "Pere", "Nuria", "Marta", "Clara",
            "Oriol", "Laia", "Adrian", "Irene", "Sergi", "Paula", "Xavier", "Elena",
            "Benjamin", "Samantha", "Samuel", "Katherine", "Frank", "Christine", "Gregory", "Debra",
            "Raymond", "Rachel", "Alexander", "Catherine", "Patrick", "Carolyn", "Jack", "Janet"
    };

    @Override
    public String get() {
        return randomElement(FIRST_NAMES);
    }
}

