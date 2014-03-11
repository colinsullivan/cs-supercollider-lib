SecondSynkopater : PerformanceEnvironmentComponent {
  var <>schedulerTask,
    <>hardSineVoicer;

  init {
    arg params;

    super.init(params);
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
      me.hardSineVoicer.trigger1(440, lat: 0);

      if (me.playing, {
        TempoClock.default.sched(1, me.schedulerTask);    
      });
    }
  }

  on_play {
    var clock = TempoClock.default(),
      nextBar = clock.nextTimeOnGrid(clock.beatsPerBar);

    super.on_play();

    // start scheduler task next bar
    clock.schedAbs(nextBar, this.schedulerTask);    
  }

  init_gui {
    arg params;

    super.init_gui(params);


  }

  init_uc33_mappings {
    super.init_uc33_mappings();
  }

}
