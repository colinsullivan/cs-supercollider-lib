DubBassEnvironment : ControllerEnvironment {

  init {
    var voicer, sock;

    super.init();

    /*voicer = MonoPortaVoicer(
      1,
      Instr("synths.DubBass"),
      [\tempo, VarLag.kr(MIDISyncClock.tempo(), warp: \linear, mul: 60)]
    );

    MIDISyncClock.play({
      var tempo = MIDISyncClock.tempo();

      "tempo:".postln;
      tempo.postln;

      1;
    }, 1);

    voicer.portaTime = 0;

    sock = VoicerMIDISocket(
      [1, 0],
      voicer
    );*/

    voicer.gui();

  }
}
