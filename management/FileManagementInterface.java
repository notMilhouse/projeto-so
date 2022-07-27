

import management.exceptions.*;
import model.FileType;

public interface FileManagementInterface{
	
	/**
     * Metodo que adiciona nova entrada (diretorio) em um diretorio existente
     * @param pathname - caminho absoluto do diretorio alvo da operacao
     * @param filename - identificador do novo diretorio sendo criado
     * @return true se operacao realizada com sucesso, false se operacao nao pode ser realizada
     * @throws InvalidEntryException - se formato de pathname ou filename for invalido ou entrada for duplicada
	 * @throws VirtualFileNotFoundException - se diretorio identificado por pathname nao for encontrado 
	*/
	public boolean addDirectory(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException;
	
	/**
     * Metodo que adiciona nova entrada (arquivo) em um diretorio existente
     * @param pathname - caminho absoluto do diretorio alvo da operacao
     * @param filename - identificador do novo arquivo sendo criado
     * @return true se operacao realizada com sucesso, false se operacao nao pode ser realizada
     * @throws InvalidEntryException - se formato de pathname ou filename for invalido, entrada for duplicada, 
	 * 		   tipo de arquivo for invalido (diretorio) ou tamanho do arquivo for invalido
	 * @throws VirtualFileNotFoundException - se diretorio identificado por pathname nao for encontrado 
     */
	 public boolean addFile(String pathname, String filename, FileType type, int length) throws InvalidEntryException, VirtualFileNotFoundException;
	
	/**
     * Metodo que exclui um arquivo/diretorio existente
     * @param pathname - caminho absoluto do diretorio alvo da operacao
     * @param filename - identificador do arquivo/diretorio sendo excluido
     * @return true se operacao realizada com sucesso, false se operacao nao pode ser realizada
     * @throws InvalidEntryException - se formato de pathname ou filename for invalido 
	 * @throws VirtualFileNotFoundException - se diretorio ou filename identificado por pathname nao for encontrado 
     */
	public boolean deleteFile(String pathname, String filename) throws InvalidEntryException, VirtualFileNotFoundException;
	
	/**
     * Metodo que lista as entradas de um diretorio existente
     * @param pathname - caminho absoluto do diretorio alvo da operacao
     * @return um array of strings (no formato apropriado) contendo a descricao de cada entrada do diretorio 
     * @throws InvalidEntryException - se formato de pathname for invalido 
	 * @throws VirtualFileNotFoundException - se diretorio identificado por pathname nao for encontrado 
     */
	public String[] listDirectory(String pathname) throws InvalidEntryException, VirtualFileNotFoundException;
	
	/**
     * Metodo que le um conjunto de comandos de gerenciamento do sistema de arquivos virtuais (no formato apropriado)   
	 * armazenado em um arquivo e executa automaticamente estes comandos
     * @param pathname - caminho do arquivo texto onde os comandos estao armazenados
     * @return true se arquivo de comandos for encontrado e processado, false se arquivo de comandos nao for encontrado 
     */
	public boolean parseCommandFile(String pathname);
	
	/**
     * Metodo que salva as estruturas do disco virtual em um arquivo binario    
     * @return true se operacao realizada com sucesso, false se erro durante a execucao da operacao 
     */ 
	public boolean saveVirtualDisk();
}