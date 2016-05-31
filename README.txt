Christopher Nelson
christopher.w.nelso@wsu.edu

-Description-
A scapegoat tree is a self balancing binary search tree. This program prints a scapegoat tree 
using input commands to create and modify the tree. It uses the highest possible ancestor in
the tree as the scapegoat. 

Input:
The input file is a list of commmands and inputs. The input file must have a new command on
each line and it must start with with the BuildTree command. This file must be called "tree.txt".

Output: 
This program outputs a file called tree.svg. This file can be opened in a web browser
(tested in firefox), which then displays the tree graphically.

Commands:
BuildTree <alpha> <key> - Builds a new tree with alpha weight and the first node containing key.
Insert <key> - Inserts a node with the key value into the tree
Search <key> - Searchs for a node with the key. The commandline states if the key is found or not.
Delete <key> - Delete a node with the key from the tree.
Print - Prints the scapegoat tree by outputting the file tree.svg.
Done - Exits the program.

-How to run-
Command Prompt (using .jar file):
1.) Unzip the files to the same directory.
2.) Make sure your input file (tree.txt) is in the same directory.
3.) Type the following command: java -jar scapegoat.jar
4.) The command line will output text depending on the commands in the tree.txt file.
5.) Open tree.svg in your web browser.

Eclipse (using .java files):
1.) Unzip the filws to the same directory.
2.) Make sure your input file (tree.txt) is in the same directory.
3.) Run ScapeGoatTree.java in Eclipse.
4.) The command line will output text depending on the commands in the tree.txt file.
5.) Open the tree.svg in your web browser.

-Files-
README.txt - Readme file
scapegoat.jar - runnable jar file for running in the command line
ScapeGoatTree.java - Source code for the Scapegoat Tree program
TreePrinter.java - Tree printing code (provided on class website)