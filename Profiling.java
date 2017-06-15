import java.io.File;
import java.util.Scanner;
import java.util.ArrayList;

public class Profiling {
	
	static Scanner scanner;
	static File file;
	
	static ArrayList<String> lines;
	static ArrayList<Profile> profiles;

	public static void main(String[] args) {
		file = new File("profiling-data.txt");
		lines = new ArrayList<String>();
		profiles = new ArrayList<Profile>();
		
		try {	
			scanner = new Scanner(file);
			while(scanner.hasNextLine()) {
				lines.add(scanner.nextLine());
			}
		} catch (Exception e) {
			System.out.println("File " + file + " not found!");
		}
		addProfiles(extractNames(), extractBooks());
		queryBooksFromReader();
		queryReadersFromBook();
		getRecommendation();
	}
	
	public static void printLines() {
		for(String s : lines) {
			System.out.println(s);
		}
	}
	
	public static String[] splitString(String string, String splitter) {
		return string.split(splitter);
	}
	
	public static Profile createProfile(String[] strings) {
		Profile profile = new Profile();
		profile.setName(strings[0]);	
		for(int i = 1 ; i < strings.length ; ++i) {
			profile.addBook(strings[i]);
		}
		return profile;
	}
	
	public static void addProfiles(ArrayList<String> names, ArrayList<ArrayList<String>> books) {
		for(int i = 0 ; i < names.size() ; ++i) {
			Profile profile = new Profile();
			profile.setName(names.get(i));
			profile.setBooks(books.get(i));
			profiles.add(profile);
		}
	}
	
	public static ArrayList<String> extractNames() {
		ArrayList<String> names = new ArrayList<String>();
		
		for(String line : lines) {
			String[] lineStrings = splitString(line, ",");
			String[] nameStrings = splitString(lineStrings[0], " ");
			String name = "";
			
			for(int i = 0 ; i < nameStrings.length ; ++i) {
				name = name + nameStrings[i];
				if(i < nameStrings.length-2) {
					name = name + " ";
				}
			}
			names.add(name);
		}
		return names;
	}
	
	public static ArrayList<ArrayList<String>> extractBooks() {
		ArrayList<ArrayList<String>> books = new ArrayList<ArrayList<String>>();
		
		for(String line : lines) {
			String[] lineStrings = splitString(line, ",");
			ArrayList<String> booksPerReader = new ArrayList<String>();
			books.add(booksPerReader);
			
			for(int j = 1 ; j < lineStrings.length ; ++j) {
				String[] bookStrings = splitString(lineStrings[j], " ");			
				String book = "";
				
				for(int i = 0 ; i < bookStrings.length ; ++i) {
					book = book + bookStrings[i];
					if(i > 0 && i < bookStrings.length-1) {
						book = book + " ";
					}
				}
				booksPerReader.add(book);
			}
		}
		return books;
	}
	
	public static void queryBooksFromReader() {
		System.out.println("Type a (partial) reader's name to see their reading list:");
		scanner = new Scanner(System.in);
		
		if(scanner.hasNextLine()) {
			String queryName = scanner.nextLine();
			System.out.println("\nThe people with \"" + queryName + "\" in their name have read the following books:\n");
			queryName = queryName.toLowerCase();
			
			for(Profile profile : profiles) {
				String profileName = profile.getName().toLowerCase();
				if(profileName.contains(queryName)) {
					profile.print();
				}  		
			}
		}
	}
	
	public static void queryReadersFromBook() {
		System.out.println("Type a (partial) book's name to see its readers:");
		scanner = new Scanner(System.in);
		
		if(scanner.hasNextLine()) {
			String queryBook = scanner.nextLine();
			System.out.println("\nThe following people have read a book with \"" + queryBook + "\" in its title:\n");
			queryBook = queryBook.toLowerCase();
			
			for(Profile profile : profiles) {
				for(String book : profile.getBooks()) {
					String b = book.toLowerCase();
					if(b.contains(queryBook)) {
						System.out.println(profile.getName() + " (" + book + ")");
					}
				}
			}
		}
	}
	
	public static void getRecommendation() {
		System.out.println("\nType a (partial) reader name to get them a recommendation:");
		scanner = new Scanner(System.in);
		ArrayList<Profile> similarProfiles = new ArrayList<Profile>();
		
		if(scanner.hasNextLine()) {
			String queryName = scanner.nextLine();
			System.out.println("\nThe people with \"" + queryName + "\" in their name get the following recommendations:\n");
			queryName = queryName.toLowerCase();
			
			for(Profile profile : profiles) {
				String profileName = profile.getName().toLowerCase();
				if(profileName.contains(queryName)) {
					similarProfiles = determineSimilarProfiles(profile);
					String recommendation = getNonSimilarBook(profile, similarProfiles);
					if(!recommendation.equals("")) {
						System.out.println("The recommended book for " + profile.getName() + " is: " + recommendation + "!");
					} else {
						System.out.println("There are no recommendations for " + profile.getName() + "!");
					}
				}
			}
		}
	}
	
	public static ArrayList<Profile> determineSimilarProfiles(Profile profile) {
		int minSimilarBooks = 3;
		if(profile.getBooks().size() < minSimilarBooks) {
			minSimilarBooks = profile.getBooks().size();
		}
		ArrayList<Profile> similar = new ArrayList<Profile>();
		
		for(Profile p : profiles) {
			int similarBooks = 0;
			if(!p.equals(profile)) {
				for(int i = 0 ; i < profile.getBooks().size() ; ++i) {
					for(int j = 0 ; j < p.getBooks().size() ; ++j) {
						if(profile.getBooks().get(i).equals(p.getBooks().get(j))) {
							similarBooks++;
							if(similarBooks >= minSimilarBooks) {
								similar.add(p);
								break;
							}
						}
					}
				}
			}
		}		
		return similar;
	}
	
	public static String getNonSimilarBook(Profile profile, ArrayList<Profile> similarProfiles) {
		String nonSimilarBook = "";
		
		for(Profile s : similarProfiles) {
			for(String book : s.getBooks()) {
				for(String b : profile.getBooks()) {
					if(b.equals(book)) {
						break;
					}
					nonSimilarBook = book;
					return nonSimilarBook;
				}
			}
		}	
		return nonSimilarBook;
	}
}
