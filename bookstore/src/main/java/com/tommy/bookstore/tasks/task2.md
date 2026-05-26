🚀 Тепер твоє НАСТУПНЕ завдання

І воно дуже важливе.

🔥 ЗАВДАННЯ — CASCADE
🧠 Зараз ти робиш:
save(publisher)
save(magazine1)
save(magazine2)
Але ORM може:
зберегти graph автоматично
⚠️ І це одна з головних переваг Hibernate
🔥 Твоє завдання
1.

У Publisher:

додати:

cascade = CascadeType.ALL
Спробуй сам 🙂
Підказка

Ось тут:

@OneToMany(...)
має стати щось типу:
@OneToMany(..., cascade = ...)
🔥 2.

У main:

залишити тільки:

bookstoreService.createPublisher(publisher);
⚠️ І ВСЕ.
❌ Видалити:
createMagazine(...)
🔥 3.

Перевір:
чи magazines автоматично збережуться.

🧠 Якщо cascade працює —

Hibernate сам:

пройде по graph,
побачить magazines,
зробить insert.
⚠️ І це вже:
реальний ORM behavior
🔥 Після цього буде наступна дуже важлива тема
LAZY loading
І там ти побачиш:
proxy,
delayed SQL,
LazyInitializationException 🙂
🚀 І це вже буде майже “справжній Hibernate backend” 🙂

    🚀 Краще зробити helper method
    
    У Publisher:
    
    public void addMagazine(Magazine magazine) {

    if (magazines == null) {
        magazines = new ArrayList<>();
    }

    magazines.add(magazine);

    magazine.setPublisher(this);
    }
    🔥 І тоді main стане НАБАГАТО красивішим
    Publisher dcPublisher = new Publisher("DC Comics");
    
    dcPublisher.addMagazine(
    new Magazine("Batman: Gotham Nights #11", 4.99));
    
    dcPublisher.addMagazine(
    new Magazine("Flash Speed Force Monthly #21", 3.95));
    
    dcPublisher.addMagazine(
    new Magazine("Justice League Unlimited Special #3", 7.10));
    
    BookstoreService bookstoreService = new BookstoreService();
    
    bookstoreService.createPublisher(dcPublisher);

	public static void main(String[] args) {
		Publisher marvelPublisher = new Publisher("Marvel Comics");

		marvelPublisher.addMagazine(
				new Magazine("Spider-Man: Web of Shadows #18", 5.99));

		marvelPublisher.addMagazine(
				new Magazine("Iron Man Tech Monthly #7", 6.25));

		marvelPublisher.addMagazine(
				new Magazine("Captain America Liberty Journal #12", 4.80));

		marvelPublisher.addMagazine(
				new Magazine("Thor: Asgard Chronicles #4", 7.15));

		marvelPublisher.addMagazine(
				new Magazine("Doctor Strange Mystic Arts Review #9", 6.70));

		marvelPublisher.addMagazine(
				new Magazine("Black Panther Wakanda Times #15", 5.40));

		marvelPublisher.addMagazine(
				new Magazine("X-Men Mutation Report #22", 4.95));

		marvelPublisher.addMagazine(
				new Magazine("Guardians of the Galaxy Space Digest #11", 6.90));

		BookstoreService bookstoreService = new BookstoreService();

		bookstoreService.createPublisher(marvelPublisher);

	}