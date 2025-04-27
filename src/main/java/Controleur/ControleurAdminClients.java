package controleur;

import modele.Client;
import modele.dao.ClientDAO;

import java.sql.Date;
import java.util.List;

public class ControleurAdminClients  {

    private final ClientDAO dao;

    public ControleurAdminClients(){
        this.dao = new ClientDAO();
    }

   public List<Client> getTousLesClients(){
        return dao.getAllClients();

    }

    public void ajouterClient(String mail, String mdp, String nom, String prenom, Date dateNaissance) throws Exception {
        Client nouveau = new Client(mail, mdp, nom, prenom, dateNaissance, 0);
        dao.ajouterClient(nouveau);
    }

    public void modifierClient(Client client, String mail, String mdp, String nom, String prenom, Date dateNaissance) throws Exception{

        client.setMail(mail);
        client.setMdp(mdp);
        client.setNom(nom);
        client.setPrenom(prenom);
        client.setDatedeNaissance(dateNaissance);
        dao.modifierClient(client);

    }

    public void supprimerClient(Client client) throws Exception{

        dao.supprimerClient(client.getId());
    }
}
