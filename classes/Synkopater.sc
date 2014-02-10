Synkopater : PerformanceEnvironmentComponent {
  var <>playerRoutine,
    <>synkopatedVoiceOne,
    <>synkopatedVoiceTwo;

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
      tempoBus: TempoBus.new(),
      multiplier: 0.5
    ));

    this.synkopatedVoiceTwo = Patch(synkopaterVoiceInstr, (
      delayAmt: KrNumberEditor.new(0.0, specsByName["delayAmt"]),
      phase: 0.5,
      tempoBus: TempoBus.new(),
      multiplier: 0.5
    ));
  }

  on_play {
    //this.playerRoutine.play();
    var me = this;


    TempoClock.default.playNextBar({
      me.outputChannel.play(me.synkopatedVoiceOne);
      me.outputChannel.play(me.synkopatedVoiceTwo);
    });

  }

  on_stop {
    this.playerRoutine.stop();
  }

  init_gui {
    arg params;
    var labelWidth = 75,
      layout = params['layout'];

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("one", layout, labelWidth);
      this.synkopatedVoiceOne.delayAmt.gui(layout);
      layout.startRow();

      ArgNameLabel("two", layout, labelWidth);
      this.synkopatedVoiceTwo.delayAmt.gui(layout);
      layout.startRow();

    });
  }
}
