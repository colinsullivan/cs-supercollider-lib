RhodesVoicerEnvironment : PerformanceEnvironmentComponent {
  init {
    arg params;
    var voicer,
      sock,
      gui;

    super.init(params);

    voicer = Voicer.new(8, Instr.at("fm.Rhodes"));
    sock = VoicerMIDISocket([MIDIClient.sources.indexOf(MIDIIn.findPort("(out) To SuperCollider", "(out) To SuperCollider")), 1], voicer);

    sock.addControl(7, \amp);
    gui = voicer.gui();

    this.init_gui((
      window: gui.masterLayout
    ));

  }
}
