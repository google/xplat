#!/usr/bin/env python3
"""Regenerates K2K readable golden files."""

import argparse
import os
import shutil
import subprocess


def main() -> None:
  parser = argparse.ArgumentParser(
      description="Regenerate K2K readable golden outputs."
  )
  parser.add_argument(
      "readables",
      nargs="*",
      help="Optional readable directory names to filter (e.g. library)",
  )
  args = parser.parse_args()

  k2k_test_dir = os.path.dirname(os.path.abspath(__file__))
  workspace_dir = k2k_test_dir
  while (
      not os.path.exists(os.path.join(workspace_dir, "WORKSPACE"))
      and workspace_dir != "/"
  ):
    workspace_dir = os.path.dirname(workspace_dir)

  readable_root = os.path.join(k2k_test_dir, "readable")
  readable_dirs = sorted([
      d
      for d in os.listdir(readable_root)
      if os.path.isdir(os.path.join(readable_root, d))
      and os.path.exists(os.path.join(readable_root, d, "BUILD"))
  ])
  if args.readables:
    filter_set = set(args.readables)
    readable_dirs = [d for d in readable_dirs if d in filter_set]

  if not readable_dirs:
    print("No matching readable targets found.")
    return

  rel_readable_root = os.path.relpath(readable_root, workspace_dir)
  targets = [
      f"//{rel_readable_root}/{d}:readable_golden" for d in readable_dirs
  ]
  subprocess.check_call(["blaze", "build"] + targets, cwd=workspace_dir)

  for d in readable_dirs:
    rel_pkg = os.path.join(rel_readable_root, d)
    golden_src = os.path.join(
        workspace_dir, "blaze-bin", rel_pkg, "readable_golden"
    )
    output_dir = os.path.join(readable_root, d, "output")
    if os.path.exists(output_dir):
      shutil.rmtree(output_dir)
    shutil.copytree(golden_src, output_dir)
    for root, dirs, files in os.walk(output_dir):
      for name in dirs:
        os.chmod(os.path.join(root, name), 0o755)
      for name in files:
        os.chmod(os.path.join(root, name), 0o644)
    print(f"Regenerated {d}/output")


if __name__ == "__main__":
  main()
