package exchange.fullduplex.websocket.commands;

public
class ResignGameCommand extends UserGameCommand {
    public
    ResignGameCommand(CommandType commandType, String authToken, Integer gameID) {
        super(commandType, authToken, gameID);
    }
}
