Instr("cs.utility.Passthrough", {
  arg inputChannelNums;

  SoundIn.ar(inputChannelNums);
}, [
  ObjectSpec([1])
]);
