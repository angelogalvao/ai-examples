# Instruct Lab

This is the help for installing and running Instruct Lab

1. To install it:

~~~
python3.11 -m venv --upgrade-deps venv
source venv/bin/activate
pip install 'instructlab[mps]'
~~~

1.1. Install auto completation

~~~
autoload -Uz compinit
compinit
eval "$(_ILAB_COMPLETE=zsh_source ilab)"
~~~

2. To enable the enviroment 

~~~
source venv/bin/activate
~~~

3. To disable the enviroment

~~~
deactivate
~~~


## Run examples

There are examples to try, that are listed by number:

1. 001-README.MD