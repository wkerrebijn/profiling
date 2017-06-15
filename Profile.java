import java.util.ArrayList;

public class Profile {
	private String name;
	private ArrayList<String> books;
	
	public Profile() {
		this.name = "";
		this.books = new ArrayList<String>();
	}
	
	public Profile(String name) {
		this.name = name;
		books = new ArrayList<String>();
	}
	
	public Profile(String name, ArrayList<String> books) {
		this.name = name;
		this.books = books;
	}
	
	public String getName() {
		return name;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public ArrayList<String> getBooks() {
		return books;
	}
	
	public void setBooks(ArrayList<String> books) {
		this.books = books;
	}
	
	public void addBook(String book) {
		books.add(book);
	}
	
	public void print() {
		System.out.println(name + " has read: ");
		for(String book : books) {
			System.out.println("* " + book + "; "); 
		}
		System.out.println();
	}
}
