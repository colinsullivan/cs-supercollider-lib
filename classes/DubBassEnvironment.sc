DubBassEnvironment : ControllerEnvironment {

  init {
    var voicer, sock;

    super.init();

    voicer = MonoPortaVoicer(
      1,
      Instr("synths.DubBass")
    );

    sock = VoicerMIDISocket(
      [0, 0],
      voicer
    );

    voicer.gui();

  }
}
