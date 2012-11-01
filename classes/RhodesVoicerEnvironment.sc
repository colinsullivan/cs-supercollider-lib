RhodesVoicerEnvironment : ControllerEnvironment {
  init {
    var voicer,
      sock;

    super.init();

    voicer = Voicer.new(8, Instr.at("fm.Rhodes"));
    sock = VoicerMIDISocket([MIDIClient.sources.indexOf(MIDIIn.findPort("(out) To SuperCollider", "(out) To SuperCollider")), 1], voicer);

    sock.addControl(7, \amp);
    voicer.gui();

  }
}
