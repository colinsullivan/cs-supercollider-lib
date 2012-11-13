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
          MIDIIn.findPort("(out) To SuperCollider", "(out) To SuperCollider")
        ),
        params['inChannel']
      ],
      this.voicer
    );

    gui = voicer.gui();
    
    this.init_gui((
      window: gui.masterLayout
    ));

    {this.init_done_callback.value();}.defer(1);
  }
}
