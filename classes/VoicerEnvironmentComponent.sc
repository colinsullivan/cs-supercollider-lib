VoicerEnvironmentComponent : PerformanceEnvironmentComponent {
  var <>voicer, <>sock;

  init {
    arg params;
    var gui;
    
    super.init(params);

    this.voicer = Voicer.new(params['numVoices'], params['instr']);
    this.sock = VoicerMIDISocket(
      [
        MIDIClient.sources.indexOf(
          MIDIIn.findPort("(out) SuperCollider", "(out) SuperCollider")
        ),
        params['inChannel']
      ],
      this.voicer
    );

    gui = voicer.gui();
    
    this.init_gui((
      window: gui.masterLayout
    ));

    this.init_external_controller_mappings();

    {this.init_done_callback.value();}.defer(1);
  }
}
