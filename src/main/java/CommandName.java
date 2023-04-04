public enum CommandName {
    QUIT("quit"),
    CAT("cat"),
    COPY("cp"),
    MOVE("mv"),
    SCP("scp"),
    EMPTY("");

    private final String value;

    CommandName(String value) {
        this.value = value;
    }

    public static CommandName createByValue(String value) {
        for (CommandName commandName : CommandName.values()){
            if (commandName.value.equals(value)) {
                return commandName;
            }
        }

        return CommandName.EMPTY;
    }


}
