## Example 001

When run this examples, change the path to the path in you environment. 

1. Create the config
~~~
ilab config init --profile /Users/asouza/repo/personal/ai-examples/ilab-example/001-config.yaml --taxonomy-path ./taxonomy
~~~

2. Attach your repository taxonomy (create a github repository prior to run this step)
~~~
cd taxonomy
git remote -v
git remote rm origin
git remote add origin https://github.com/angelogalvao/ai-taxonomy.git
git push --set-upstream origin main
cd ..
~~~

3. Validate path
~~~
cat ~/.config/instructlab/config.yaml
ilab config show -k generate.taxonomy_path
ilab taxonomy diff
~~~

4. Downlaod models

~~~
ilab model download
ilab model list
~~~

5. Chat with the mdoel

~~~
ilab model chat --model /Users/asouza/.cache/instructlab/models/granite-7b-lab-Q4_K_M.gguf
~~~

6. Interact with the chat model.