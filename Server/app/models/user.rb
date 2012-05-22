class User < ActiveRecord::Base
	has_many :colors
 	attr_accessible :email, :id_avatar, :keys, :name, :num_done, :num_success, :piggies
end
