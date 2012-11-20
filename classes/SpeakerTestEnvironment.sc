SpeakerTestEnvironment : PatchEnvironment {

  load_patch {
    super.load_patch();

    this.patch = Patch(Instr.at("util.EnvelopedWhiteNoise"))

  }
}
