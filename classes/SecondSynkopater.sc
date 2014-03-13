SecondSynkopater : PerformanceEnvironmentComponent {
  var <>schedulerTask,
    <>hardSineVoicer,
    <>quantizationStep;

  init {
    arg params;

    super.init(params);

    this.quantizationStep = KrNumberEditor(1, ControlSpec(1, 8, step: 1));
  }

  init_tracks {
    super.init_tracks();
  }

  init_patches {
    super.init_patches();

    this.hardSineVoicer = Voicer.new(
      2,
      Instr("cs.synths.HardSine")
    );

  }

  play_patches_on_tracks {
    super.play_patches_on_tracks();
    
    this.hardSineVoicer.target_(this.outputChannel);
  }

  load_environment {
    var me = this;
    super.load_environment();

    this.schedulerTask = {
      var nextTriggerTime,
        t = TempoClock.default,
        notePhase,
        noteBeat,
        noteLatency;

      //"--------------".postln();
      //"scheduler task".postln();

      // schedule notes
      for (0, this.quantizationStep.value - 1, {
        arg i;

        notePhase = (i / this.quantizationStep.value) * t.beatsPerBar;
        noteBeat = t.beatsPerBar + t.nextTimeOnGrid(
          t.beatsPerBar,
          notePhase
        );
        noteLatency = t.beats2secs(noteBeat) - t.seconds;
        
        me.hardSineVoicer.trigger1(
          440 * 16,
          lat: noteLatency
        );

      });

      // return value, schedule notes again next bar
      if (me.playing, {
        TempoClock.default.beatsPerBar;
      }, {
        nil;
      });
    }
  }

  on_play {
    var clock = TempoClock.default(),
      nextBar = clock.nextTimeOnGrid(clock.beatsPerBar);

    super.on_play();

    // start scheduler task next bar
    "calling schedAbs to start scheduler task".postln();
    clock.schedAbs(nextBar, this.schedulerTask);    
  }

  init_gui {
    arg params;
    
    var layout = params['layout'],
      labelWidth = 80,
      me = this;

    super.init_gui(params);

    layout.flow({
      arg layout;

      ArgNameLabel("quantizationStep", layout, labelWidth);
      me.quantizationStep.gui(layout);
      layout.startRow();

    });

  }

  init_uc33_mappings {
    super.init_uc33_mappings();
  }

}
