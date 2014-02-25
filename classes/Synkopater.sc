Synkopater : PerformanceEnvironmentComponent {
  var <>playerRoutine,
    <>submixOne,
    <>submixTwo,
    <>synkopatedVoiceOne,
    <>synkopatedVoiceTwo,
    <>synkopatedDelayOne,
    <>synkopatedDelayTwo,
    <>synkopationControlOne,
    <>synkopationControlTwo,
    <>triggerDelayOne,
    <>triggerDelayTwo,
    <>playTask;

  init {
    arg params;

    super.init(params);

    this.triggerDelayOne = 0.0;
    this.triggerDelayTwo = 0.0;
  }

  load_environment {
    var impulseInstr = Instr("cs.percussion.Impulsive"),
      //synkopaterVoiceInstrArgNames = synkopaterVoiceInstr.argNames,
      //synkopaterVoiceInstrSpecs = synkopaterVoiceInstr.specs,
      //specsByName = (),
      me = this;

    super.load_environment();

    this.playerRoutine = Routine.new({
      loop {
        "Hello!".postln();

        2.0.wait();
      }
    });

    //synkopaterVoiceInstrArgNames.do({
      //arg argName, argIndex;

      //specsByName[argName] = synkopaterVoiceInstrSpecs[argIndex];
    //});

    this.submixOne = MixerChannel.new(
      this.gui_window_title() ++ "-one",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );

    this.submixTwo = MixerChannel.new(
      this.gui_window_title() ++ "-two",
      Server.default,
      2, 2,
      outbus: this.outputChannel
    );

    /*this.synkopatedVoiceOne = Patch(synkopaterVoiceInstr, (
      phase: KrNumberEditor.new(0.0, specsByName["phase"]),
      tempoBus: TempoBus.new(),
      multiplier: 0.5
    ));*/
    this.synkopatedVoiceOne = Voicer.new(
      2,
      impulseInstr,
      target: this.submixOne
    );

    /*this.synkopatedVoiceTwo = Patch(synkopaterVoiceInstr, (
      phase: KrNumberEditor.new(0.5, specsByName["phase"]),
      tempoBus: TempoBus.new(),
      multiplier: 0.5
    ));*/
    
    this.synkopatedVoiceTwo = Voicer.new(
      2,
      impulseInstr,
      target: this.submixTwo
    );

    this.synkopatedDelayOne = FxPatch("cs.Synkopater.SynkopaterDelay", (
      numChan: 2,
      delaySecs: KrNumberEditor.new(0.0, ControlSpec(0.0, 8.0))
    ));

    this.synkopatedDelayTwo = FxPatch("cs.Synkopater.SynkopaterDelay", (
      numChan: 2,
      delaySecs: KrNumberEditor.new(0.0, ControlSpec(0.0, 8.0))
    ));

    this.synkopationControlOne = KrNumberEditor.new(0.0, \unipolar);
    this.synkopationControlTwo = KrNumberEditor.new(0.0, \unipolar);

    this.synkopationControlOne.action = {
      arg val;
      var currentBeatsPerSecond = Tempo.tempo;

      me.synkopatedDelayOne.delaySecs.value = currentBeatsPerSecond + (currentBeatsPerSecond * val);
      me.triggerDelayOne = currentBeatsPerSecond * val;
    };

    this.synkopationControlTwo.action = {
      arg val;
      var currentBeatsPerSecond = Tempo.tempo;

      me.synkopatedDelayTwo.delaySecs.value = currentBeatsPerSecond + (currentBeatsPerSecond * val);
      me.triggerDelayTwo = currentBeatsPerSecond * val;
    };

    this.playTask = {
      me.synkopatedVoiceOne.trigger1(440, lat: me.triggerDelayOne);
      me.synkopatedVoiceTwo.trigger1(880, lat: me.triggerDelayTwo);

      TempoClock.default.sched(1, me.playTask);
    };
  }

  on_play {
    //this.playerRoutine.play();
    var me = this;

    this.submixOne.playfx(this.synkopatedDelayOne);
    this.submixTwo.playfx(this.synkopatedDelayTwo);

    /*TempoClock.default.playNextBar({
      me.submixOne.play(me.synkopatedVoiceOne);
      me.submixTwo.play(me.synkopatedVoiceTwo);
    });*/

    TempoClock.default.playNextBar(this.playTask);

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
      this.synkopationControlOne.gui(layout);
      layout.startRow();

      ArgNameLabel("two", layout, labelWidth);
      this.synkopationControlTwo.gui(layout);
      layout.startRow();

    });
  }
}
