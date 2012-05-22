class Color < ActiveRecord::Base
  belongs_to :user
  attr_accessible :hex

  validates :user_id,  :presence => true
  validates :hex,  :presence => true
end
