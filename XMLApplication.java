/*
 * The purpose of this program is to create a musical playlist in the form of a XML file that user's can display, search or add to. 
 * By using the concept of java's Elements and Element in terms of parent and child elements. 
 * The hierarchy starts off with "music" as the root, then goes into playlist which is filled with different songs with information on the artist, album and the song’s name.
 * Created by Yolanda Yu
 * Last Modified on 05/26/2017
 */

//imports of XMLApplication class
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;
import nu.xom.*;

//music, play list, artist, album

public class XMLApplication
{
  // fields of XMLApplication class
  private static Elements music;
  private static Element playlist = new Element("playlist");
  private static Document doc = new Document(playlist);
  private static File myPlaylist = new File("myplaylist.xml");

  // main method of XMLApplication class
  public static void main(String[] args)
  {
    // Calls the action method to start off the program by asking the user what
    // they want to do
    actions();
  }

  // this method asks the user whether or not they want to perform another
  // action
  public static void repeat()
  {
    System.out.println("Do you want to perform another action?" + "\n" + "  Yes or No");
    Scanner one = new Scanner(System.in);
    String response = one.nextLine();

    // if the user answered yes, the the program asks what the user wants to do,
    // if answered no then the program closes
    if (response.equalsIgnoreCase("Yes"))
    {
      actions();
    }
    else
    {
      one.close();
      return;
    }
  }

  // Utilizes a scanner that lets the user choose what they want to do with the
  // program. They can either add a new song, search the playlist or display a
  // list of music.
  public static void actions()
  {
    // calls the file method, which pulls up and reads the corresponding
    // document
    file();
    // asks the user for an input
    System.out.println("Do you want to display, search or add a new song?" + "\n"
        + "   Display- display a list of all the songs in the playlist" + "\n" + "   Search- search for a song" + "\n"
        + "   Add- add a new song to the playlist" + "\n" + "   Exit- exit the program");

    Scanner two = new Scanner(System.in);
    String input = two.nextLine(); // the user’s answer

    // depending on the response, the program will call its respective method to
    // perform the certain action.
    if (input.equalsIgnoreCase("display"))
    {
      display();
    }
    else if (input.equalsIgnoreCase("search"))
    {
      search();

    }
    else if (input.equalsIgnoreCase("add"))
    {
      addSong();
    }
    else if (input.equalsIgnoreCase("exit"))
    {
      two.close();
      return; // the program closes if the user inputs "exit"
    }
    else
    {
      System.out.println("Try Again"); // if what the user typed was not an
      // option, then the system asks the
      // question again by calling the main
      // method
      main(null);
    }
  }

  // formatting and displaying the XML file by using serializer, using try-catch
  // to get rid of any exceptions and holes in the system
  public static void display()
  {
    // Formats the XML into a certain frame and outputs it
    try
    {
      Serializer serializer = new Serializer(System.out);
      serializer.setIndent(4);
      serializer.setMaxLength(64);
      serializer.write(doc);
    }

    // Output an error message to the user if the try block cannot be executed
    // properly
    catch (IOException ex)
    {
      System.err.println(ex);
    }
    // Calls action to ask if the user wants to perform any other action
    repeat();
  }

  // This method searches the playlist and outputs information on any song the
  // user is searching for
  public static void search()
  {
    // Initialize a new scanner that gets the song the user wants to search for
    System.out.println("What song do you want to search for?");
    Scanner three = new Scanner(System.in);
    String songTitle = three.nextLine();

    // a count for every time the song title is found in the playlist
    int correct = 0;

    // run through the entire list and output the desired song's information if
    // found
    for (int list = 0; list < music.size(); list++)
    {
      // to see if the user's input matches any song title in the playlist
      if (music.get(list).getFirstChildElement("title").getValue().equals(songTitle))
      {
        // print it out to the console
        System.out.println(music.get(list).toXML());
        correct++;
      }
    }

    // if no song title matches what the user is searching for, then the system
    // outputs "This song is not found"
    if (correct == 0)
    {
      System.out.println("This song is not found");
    }
    // calls repeat method to ask the user if they want to perform another
    // action or not
    repeat();
    three.close();
  }

  // This method lets the user add a new song to their playlist by inputting the
  // song’s artist, album and title. The structure is created by adding these
  // sub-elements to the main playlist element
  public static void addSong()
  {

    // uses a scanner to get the user’s inputs
    Scanner four = new Scanner(System.in);
    Element song = new Element("song");
    playlist.appendChild(song); // playlist is the parent of song

    System.out.println("Input the new song's artist");
    String artistName = four.nextLine(); // user’s input
    Element artist = new Element("artist");
    artist.appendChild(artistName); // putting artistName in artist
    song.appendChild(artist); // artist is a sub-element of song

    System.out.println("Input the new song's album");
    String albumName = four.nextLine(); // user’s input
    Element album = new Element("album");
    album.appendChild(albumName); // assigning albumName to album
    song.appendChild(album); // album is a sub-element of song

    System.out.println("Input the new song's title");
    String songName = four.nextLine(); // user;s input
    Element title = new Element("title");
    title.appendChild(songName); // assigning songName to title
    song.appendChild(title); // title is a sub-element of song

    // writes the information into the document, with the added on songs too
    writeFile();
    // Calls the display method which displays the whole sorted and organized
    // document
    display();
    // Calls action method again to ask if the user wants to perform another
    // action
    repeat();
    four.close();
  }

  // This method reads then builds on to the XML file containing the root and
  // all of its child elements
  public static void file()
  {
    Builder builder = new Builder();

    try
    {
      doc = builder.build(myPlaylist);
      playlist = doc.getRootElement();
      music = playlist.getChildElements(); // add everything to the music root
    }

    // If the try block cannot be executed properly due to errors, it will
    // output an according message to the user
    catch (IOException e)
    {
      System.out.println("Error: " + e);
    }
    catch (ParsingException e)
    {
      System.out.println("Error: " + e);
    }
  }

  // This method either creates a new XML file or searches for one that already
  // exist with the title. If it does exist, it takes the original file, copies
  // everything and save it in a new XML file with the new songs added.
  // Replacing/overriding the old document with the new one
  public static void writeFile()
  {
    // try-catch block that either finds or creates a XML file with a specific
    // name, if an error occurs then catch it and display an error message to
    // the user
    try
    {
      FileWriter xmlfile = new FileWriter(myPlaylist);
      BufferedWriter writer = new BufferedWriter(xmlfile);
      writer.write(doc.toXML());
      writer.close();
    }
    catch (IOException e)
    {
      System.out.println("Error: " + e);
    }
  }
}
