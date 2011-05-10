# This is the makefile written in perl
# Author: Yufei Liu

my $arg = shift @ARGV;

if ($arg eq "clean") {
    `rm parser/*.java`;
    `rm parser/*.class`;
    print "All parser-related java files and class files have been removed\n";
    exit;
}

if ($arg eq "compile") {
    print `java -jar parser/resources/jacc.jar -v parser/GPL.jacc`;
    print `javac parser/GPLParser.java`;
    print `javac helper/Preprocessor.java`;
    exit;
}


if (defined $arg) {
print `java helper.Preprocessor $arg toParse.gpl`;
print `cat toParse.gpl | java parser.Main $arg`;
} else {
print `java parser.Main`;
}
