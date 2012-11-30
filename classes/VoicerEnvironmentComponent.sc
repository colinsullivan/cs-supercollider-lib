VoicerEnvironmentComponent : PerformanceEnvironmentComponent {
  var <>voicer, <>sock, <>inChannel;

  init {
    arg params;
    var gui;
    
    super.init(params);

    this.inChannel = params['inChannel'];

    this.voicer = Voicer.new(params['numVoices'], params['instr']);

    gui = voicer.gui();
    
    this.init_gui((
      window: gui.masterLayout
    ));

    this.init_external_controller_mappings();

    {this.init_done_callback.value();}.defer(1);
  }

  init_external_controller_mappings {
    super.init_external_controller_mappings();

    this.sock = VoicerMIDISocket(
      [
        MIDIClient.sources.indexOf(
          MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider")
        ),
        this.inChannel
      ],
      this.voicer
    );
  
  }
}
