Synkopater : PerformanceEnvironmentComponent {
  var <>playerRoutine,
    <>synkopatedVoiceOne;

  init {
    arg params;

    super.init(params);


  }

  load_environment {
    var synkopaterVoiceInstr = Instr("cs.Synkopater.SynkopaterVoice"),
      synkopaterVoiceInstrArgNames = synkopaterVoiceInstr.argNames,
      synkopaterVoiceInstrSpecs = synkopaterVoiceInstr.specs,
      specsByName = ();

    super.load_environment();

    this.playerRoutine = Routine.new({
      loop {
        "Hello!".postln();

        2.0.wait();
      }
    });

    synkopaterVoiceInstrArgNames.do({
      arg argName, argIndex;

      specsByName[argName] = synkopaterVoiceInstrSpecs[argIndex];
    });

    this.synkopatedVoiceOne = Patch(synkopaterVoiceInstr, (
      delayAmt: KrNumberEditor.new(0.0, specsByName["delayAmt"]),
      gate: BeatClockPlayer(4)
    ));
  }

  on_play {
    //this.playerRoutine.play();
    this.outputChannel.play(this.synkopatedVoiceOne);
  }

  on_stop {
    this.playerRoutine.stop();
  }
}
