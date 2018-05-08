package service;

import lombok.extern.slf4j.Slf4j;
import org.snmp4j.TransportMapping;
import org.snmp4j.agent.BaseAgent;
import org.snmp4j.agent.CommandProcessor;
import org.snmp4j.agent.DuplicateRegistrationException;
import org.snmp4j.agent.ManagedObject;
import org.snmp4j.agent.mo.MOAccessImpl;
import org.snmp4j.agent.mo.MOScalar;
import org.snmp4j.agent.mo.MOTableRow;
import org.snmp4j.agent.mo.snmp.*;
import org.snmp4j.agent.security.MutableVACM;
import org.snmp4j.mp.MPv3;
import org.snmp4j.security.SecurityLevel;
import org.snmp4j.security.SecurityModel;
import org.snmp4j.security.USM;
import org.snmp4j.smi.*;
import org.snmp4j.transport.TransportMappings;

import java.io.File;
import java.io.IOException;
import java.util.Random;

@Slf4j
public class SNMPAgent extends BaseAgent {

    private String address;

    static final OID maxRate = new OID( ".1.3.6.1.2.1.4.10.0" );
    static final OID actPkts = new OID( ".1.3.6.1.3.1.5.8.0" );
    static final OID currentTraffic = new OID(".1.3.6.1.4.1.44.8");
    static Integer maxRate_value = 0;
    static Integer actPkts_value = 0;
    static Integer table1_value1 = 0;
    static Integer table1_value2 = 0;
    static Integer table1_value3 = 0;
    static Integer table1_value4 = 0;
    static Integer table1_value5 = 0;
    static Integer table1_value6 = 0;
    static Integer table1_value7 = 0;
    static Integer table1_value8 = 0;
    static Integer table1_value9 = 0;
    static Integer table1_value10 = 0;
    Random rand = new Random();

    /**
     *
     * @param address
     * @throws IOException
     */
    public SNMPAgent(String address) throws IOException {

        /**
         * Creates a base agent with boot-counter, config file, and a
         * CommandProcessor for processing SNMP requests. Parameters:
         * "bootCounterFile" - a file with serialized boot-counter information
         * (read/write). If the file does not exist it is created on shutdown of
         * the agent. "configFile" - a file with serialized configuration
         * information (read/write). If the file does not exist it is created on
         * shutdown of the agent. "commandProcessor" - the CommandProcessor
         * instance that handles the SNMP requests.
         */
        super(new File("conf.agent"), new File("bootCounter.agent"),
                new CommandProcessor(
                        new OctetString(MPv3.createLocalEngineID())));
        this.address = address;
    }

    public void updateValues()
    {
        generateNewValues();
        clearRegistry();
        registerManagedObjects();
    }

    public void clearRegistry() {
        server.getRegistry().entrySet().clear();
    }

    private void generateNewValues()
    {
        maxRate_value += rand.nextInt(50) + 1;
        System.out.println("maxRate new value is " + maxRate_value );

        actPkts_value += rand.nextInt(10) + 1;
        System.out.println("actPkts new value is " + actPkts_value );

        table1_value1 += rand.nextInt(5) + 1;
        table1_value2 += rand.nextInt(5) + 1;
        table1_value3 += rand.nextInt(5) + 1;
        table1_value4 += rand.nextInt(5) + 1;
        table1_value5 += rand.nextInt(5) + 1;
        table1_value6 += rand.nextInt(5) + 1;
        table1_value7 += rand.nextInt(5) + 1;
        table1_value8 += rand.nextInt(5) + 1;
        table1_value9 += rand.nextInt(5) + 1;
        table1_value10 += rand.nextInt(5) + 1;
    }

    /**
     * Adds community to security name mappings needed for SNMPv1 and SNMPv2c.
     */
    @Override
    protected void addCommunities(SnmpCommunityMIB communityMIB) {
        Variable[] com2sec = new Variable[] { new OctetString("public"),
                new OctetString("cpublic"), // security name
                getAgent().getContextEngineID(), // local engine ID
                new OctetString("public"), // default context name
                new OctetString(), // transport tag
                new Integer32(StorageType.nonVolatile), // storage type
                new Integer32(RowStatus.active) // row status
        };
        MOTableRow row = communityMIB.getSnmpCommunityEntry().createRow(
                new OctetString("public2public").toSubIndex(true), com2sec);
        communityMIB.getSnmpCommunityEntry().addRow( (SnmpCommunityMIB.SnmpCommunityEntryRow) row );

    }

    /**
     * Adds initial notification targets and filters.
     */
    @Override
    protected void addNotificationTargets(SnmpTargetMIB arg0,SnmpNotificationMIB arg1) {
        // TODO Auto-generated method stub

    }

    /**
     * Adds all the necessary initial users to the USM.
     */
    @Override
    protected void addUsmUser(USM arg0) {
        // TODO Auto-generated method stub

    }

    /**
     * Adds initial VACM configuration.
     */
    @Override
    protected void addViews(VacmMIB vacm) {
        vacm.addGroup(SecurityModel.SECURITY_MODEL_SNMPv2c, new OctetString(
                        "cpublic"), new OctetString("v1v2group"),
                StorageType.nonVolatile);

        vacm.addAccess(new OctetString("v1v2group"), new OctetString("public"),
                SecurityModel.SECURITY_MODEL_ANY, SecurityLevel.NOAUTH_NOPRIV,
                MutableVACM.VACM_MATCH_EXACT, new OctetString("fullReadView"),
                new OctetString("fullWriteView"), new OctetString(
                        "fullNotifyView"), StorageType.nonVolatile);

        vacm.addViewTreeFamily(new OctetString("fullReadView"), new OID("1.3"),
                new OctetString(), VacmMIB.vacmViewIncluded,
                StorageType.nonVolatile);

    }

    /**
     * Unregister the basic MIB modules from the agent's MOServer.
     */
    @Override
    protected void unregisterManagedObjects()
    {
        //unregisterManagedObject();
    }

    /**
     * Register additional managed objects at the agent's server.
     */
    @Override
    protected void registerManagedObjects()
    {
        MOScalar mo_maxRate = MOCreator.createReadOnly( maxRate, String.valueOf( maxRate_value ));
        registerManagedObject(mo_maxRate);

        MOScalar mo_actPkts = MOCreator.createReadOnly( actPkts, String.valueOf( actPkts_value ));
        registerManagedObject(mo_actPkts);

        MOTableBuilder builder = new MOTableBuilder(currentTraffic)
                .addColumnType(SMIConstants.SYNTAX_OCTET_STRING,MOAccessImpl.ACCESS_READ_WRITE)
                .addRowValue(new OctetString( String.valueOf( table1_value1 )))
                .addRowValue(new OctetString( String.valueOf( table1_value2 )))
                .addRowValue(new OctetString( String.valueOf( table1_value3 )))
                .addRowValue(new OctetString( String.valueOf( table1_value4 )))
                .addRowValue(new OctetString( String.valueOf( table1_value5 )))
                .addRowValue(new OctetString( String.valueOf( table1_value6 )))
                .addRowValue(new OctetString( String.valueOf( table1_value7 )))
                .addRowValue(new OctetString( String.valueOf( table1_value8 )))
                .addRowValue(new OctetString( String.valueOf( table1_value9 )))
                .addRowValue(new OctetString( String.valueOf( table1_value10 )));
        registerManagedObject(builder.build());

    }

    protected void initTransportMappings() throws IOException {
        transportMappings = new TransportMapping[1];
        Address addr = GenericAddress.parse(address);
        TransportMapping tm = TransportMappings.getInstance()
                .createTransportMapping(addr);
        transportMappings[0] = tm;
    }

    /**
     * Start method invokes some initialization methods needed to start the
     * agent
     *
     * @throws IOException
     */
    public void start() throws IOException {

        init();
        // This method reads some old config from a file and causes
        // unexpected behavior.
        // loadConfig(ImportModes.REPLACE_CREATE);
        addShutdownHook();
        getServer().addContext(new OctetString("public"));
        finishInit();
        run();
        sendColdStartNotification();
    }

    /**
     * Clients can register the MO they need
     */
    public void registerManagedObject(ManagedObject mo) {
        try {
            //server.unregister( mo, null);
            server.register(mo, null);
        } catch (DuplicateRegistrationException ex) {
            throw new RuntimeException(ex);
        }
    }

    public void unregisterManagedObject(ManagedObject mo) {
        server.unregister(mo, null);
    }





}