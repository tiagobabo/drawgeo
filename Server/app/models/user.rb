class User < ActiveRecord::Base
	has_many :colors
 	attr_accessible :email, :id_avatar, :keys, :name, :num_done, :num_success, :piggies, :ranking, :num_created
 	before_save :default_values

  def default_values
    self.id_avatar ||= 1
    self.keys ||= 0
    self.name ||= self.email.split('@')[0]
    self.num_done ||= 0
    self.num_success ||= 0
    self.piggies ||= 0
    self.ranking ||= 0
  end
end
