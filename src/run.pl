my $opt = $ARGV[0];

if ($opt eq "clean") {
    `rm parser/*.java`;
    `rm parser/*.class`;
    print "All parser-related java files and class files have been removed\n";
    exit;
}

print `java -jar parser/resources/jacc.jar parser/GPL.jacc`;
print `javac parser/GPLParser.java`;
print `cat parser/test.gpl | java parser.Main`;

